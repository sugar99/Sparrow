package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.domain.SearchRequestParams;
import com.micerlab.sparrow.domain.SearchType;
import com.micerlab.sparrow.domain.TimeRangeKeyCount;
import com.micerlab.sparrow.utils.Page;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.TopHits;
import org.elasticsearch.search.aggregations.metrics.TopHitsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Component
public class SearchResultDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchResultDao.class);
    
    private RestHighLevelClient elasticsearchClient;
    
    private final Map<String, Integer> createdtimeRangeKey2No = new HashMap<>();
    private final Map<String, Integer> modifiedtimeRangeKey2No = new HashMap<>();
    
    public SearchResultDao(RestHighLevelClient elasticsearchClient)
    {
        this.elasticsearchClient = elasticsearchClient;
    }
    
    /*
    {
        "query": {
            "bool": {
                "must": {
                    "multi_match": {
                        "query": "算法",
                        "fields": [
                            "title",
                            "title.cn^3",
                            "desc",
                            "desc.cn"
                        ]
                    }
                },
                "filter": [
                    {
                        "term": {
                            "type": "image"
                        }
                    },
                    {
                        "terms_set": {
                            "tags": {
                                "terms": [
                                    133,
                                    137
                                ],
                                "minimum_should_match_script": {
                                    "source": "2"
                                }
                            }
                        }
                    },
                    {
                        "terms_set": {
                            "categories": {
                                "terms": [
                                    0,
                                    6
                                ],
                                "minimum_should_match_script": {
                                    "source": "2"
                                }
                            }
                        }
                    }
                ]
            }
        },
        "aggs": {
            "exts_limit": {
                "filter": {
                    "terms": {
                        "ext": [
                            "jpg",
                            "gif"
                        ]
                    }
                },
                "aggs": {
                    "created_time_range": {
                        "date_range": {
                            "field": "created_time",
                            "format": "yyyy-MM-dd",
                            "time_zone": "+8",
                            "ranges": [
                                {
                                    "key": "三天内",
                                    "from": "now-3d/d"
                                },
                                {
                                    "key": "一周内",
                                    "from": "now-1w/d"
                                },
                                {
                                    "key": "一个月内",
                                    "from": "now-1M/d"
                                },
                                {
                                    "key": "三个月内",
                                    "from": "now-3M/d"
                                },
                                {
                                    "key": "半年内",
                                    "from": "now-6M/d"
                                },
                                {
                                    "key": "一年内",
                                    "from": "now-1y/d"
                                },
                                {
                                    "key": "全部",
                                    "to": "now"
                                }
                            ]
                        }
                    },
                    "modified_time_range": {
                        "date_range": {
                            "field": "modified_time",
                            "format": "yyyy-MM-dd",
                            "time_zone": "+8",
                            "ranges": [
                                {
                                    "key": "三天内",
                                    "from": "now-3d/d"
                                },
                                {
                                    "key": "一周内",
                                    "from": "now-1w/d"
                                },
                                {
                                    "key": "一个月内",
                                    "from": "now-1M/d"
                                },
                                {
                                    "key": "三个月内",
                                    "from": "now-3M/d"
                                },
                                {
                                    "key": "半年内",
                                    "from": "now-6M/d"
                                },
                                {
                                    "key": "一年内",
                                    "from": "now-1y/d"
                                },
                                {
                                    "key": "全部",
                                    "to": "now"
                                }
                            ]
                        }
                    }
                }
            },
            "time_limit": {
                "filter": {
                    "bool": {
                        "filter": [
                            {
                                "range": {
                                    "created_time": {
                                        "time_zone": "+8",
                                        "lte": "now"
                                    }
                                }
                            },
                            {
                                "range": {
                                    "modified_time": {
                                        "time_zone": "+8",
                                        "lte": "now"
                                    }
                                }
                            }
                        ]
                    }
                },
                "aggs": {
                    "group_by_ext": {
                        "terms": {
                            "field": "ext"
                        }
                    },
                    "results": {
                        "top_hits": {
                            "from": 0,
                            "size": 10,
                            "_source": [
                                "id",
                                "title",
                                "desc",
                                "type",
                                "ext",
                                "tags",
                                "categories",
                                "thumbnail",
                                "store_key",
                                "created_time",
                                "modified_time",
                                "version"
                            ]
                        }
                    }
                }
            }
        }
    }
     */
    public Map<String, Object> searchResults(SearchRequestParams params) throws IOException
    {
        SearchRequest searchRequest = new SearchRequest(SparrowIndex.SPA_FILES.getIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        
        query(searchSourceBuilder, params);
        aggs(searchSourceBuilder, params);
        logger.debug("search result query dsl: " + searchSourceBuilder.toString());
        
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        
        Map<String, Object> data = new HashMap<>();
        
        Aggregations aggregations = searchResponse.getAggregations();
        
        Filter exts_limit = aggregations.get("exts_limit");
        ParsedDateRange created_time_ranges = exts_limit.getAggregations().get("created_time_ranges");
        List<TimeRangeKeyCount> group_by_created_time = new LinkedList<>();
        for (Range.Bucket bucket : created_time_ranges.getBuckets())
        {
            group_by_created_time.add(
                    new TimeRangeKeyCount(
                            bucket.getKeyAsString(),
                            bucket.getDocCount(),
                            (ZonedDateTime) bucket.getFrom(),
                            bucket.getFromAsString(),
                            (ZonedDateTime) bucket.getTo(),
                            bucket.getToAsString()
                    ).no(createdtimeRangeKey2No.get(bucket.getKeyAsString()))
            );
        }
        Collections.sort(group_by_created_time);
        data.put("group_by_created_time", group_by_created_time);
        
        ParsedDateRange modified_time_ranges = exts_limit.getAggregations().get("modified_time_ranges");
        List<TimeRangeKeyCount> group_by_modified_time = new LinkedList<>();
        for (Range.Bucket bucket : modified_time_ranges.getBuckets())
        {
            group_by_modified_time.add(
                    new TimeRangeKeyCount(
                            bucket.getKeyAsString(),
                            bucket.getDocCount(),
                            (ZonedDateTime)  bucket.getFrom(),
                            bucket.getFromAsString(),
                            (ZonedDateTime)  bucket.getTo(),
                            bucket.getToAsString()
                    ).no(modifiedtimeRangeKey2No.get(bucket.getKeyAsString()))
            );
        }
        Collections.sort(group_by_modified_time);
        data.put("group_by_modified_time", group_by_modified_time);
        
        Filter time_ranges_limit = aggregations.get("time_ranges_limit");
        TopHits result = time_ranges_limit.getAggregations().get("results");
        List<Object> spaFiles = new LinkedList<>();
        SearchHit[] searchHits = result.getHits().getHits();
        for (SearchHit searchHit : searchHits)
        {
            spaFiles.add(searchHit.getSourceAsMap());
        }
        data.put("results", spaFiles);
        
        Terms group_by_ext = time_ranges_limit.getAggregations().get("group_by_ext");
        List<Map<String, Object>> extCounts = new LinkedList<>();
        for (Terms.Bucket bucket : group_by_ext.getBuckets())
        {
            Map<String, Object> extBucket = new HashMap<>();
            extBucket.put("doc_count", bucket.getDocCount());
            extBucket.put("key", bucket.getKeyAsString());
            extCounts.add(extBucket);
        }
        data.put("group_by_ext", extCounts);
        
        return data;
    }
    
    private static void query(SearchSourceBuilder searchSourceBuilder, SearchRequestParams params)
    {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        
        // [过滤/匹配]条件1 <type> 匹配文件类型
        if (!SearchType.ALL.equals(params.getSearchType()))
            boolQueryBuilder.filter().add(QueryBuilders.termQuery("type", params.getType()));
        
        // [过滤/匹配]条件2 <keyword> 匹配文件标题、文件描述
        if (!StringUtils.isEmpty(params.getKeyword()))
        {
            Map<String, Float> fields = new HashMap<>();
            fields.put("title", 1f);
            fields.put("title.cn", 3f);
            fields.put("desc", 1f);
            fields.put("desc.cn", 3f);
            boolQueryBuilder.must(
                    QueryBuilders.multiMatchQuery(params.getKeyword()).fields(fields)
                            .type(MultiMatchQueryBuilder.Type.MOST_FIELDS)
            );
        }
        
        // [过滤/匹配]条件3 <tags> 过滤标签
        // check tags exist?
        // 如果输入的标签id在ES中不存在，会导致搜索结果为空
        // 不过好像没必要检查标签id是否合法，用户自己瞎折腾，得到的结果也是不正确的
        List<Long> tags = params.getTags();
        if (tags != null && !tags.isEmpty())
        {
            boolQueryBuilder.filter().add(
                    new TermsSetQueryBuilder("tags", tags).
                            setMinimumShouldMatchScript(
                                    new Script(String.valueOf(tags.size())
                                    )
                            )
            );
        }
        
        // [过滤/匹配]条件4 <categories> 过滤类目
        // check categories exist??
        // 如果输入的类目id在ES中不存在，会导致搜索结果为空
        // 不过好像没必要检查类目id是否合法，用户自己瞎折腾，得到的结果也是不正确的
        List<Long> categories = params.getCategories();
        if (categories != null && !categories.isEmpty())
        {
            boolQueryBuilder.filter().add(
                    new TermsSetQueryBuilder("categories", categories).
                            setMinimumShouldMatchScript(
                                    new Script(String.valueOf(categories.size())
                                    )
                            )
            );
        }
        
        searchSourceBuilder.query(boolQueryBuilder);
    }
    
    private void aggs(SearchSourceBuilder searchSourceBuilder, SearchRequestParams params)
    {
        String time_zone = params.getTime_zone();
        
        // Agg1.1 created_time_ranges
        DateRangeAggregationBuilder created_time_ranges = buildTimeRangesAgg(
                "created_time_ranges",
                "created_time",
                time_zone,
                params.getCreated_time(),
                createdtimeRangeKey2No
        );
        
        // Agg1.2 modified_time_ranges
        DateRangeAggregationBuilder modified_time_ranges = buildTimeRangesAgg(
                "modified_time_ranges",
                "modified_time",
                time_zone,
                params.getModified_time(),
                modifiedtimeRangeKey2No
        );
        
        // Agg1 exts_limit
        QueryBuilder ext_filter;
        if (params.getExts().contains("all"))
            ext_filter = QueryBuilders.matchAllQuery();
        else
        {
            params.getExts().remove("all");
            ext_filter = QueryBuilders.termsQuery("ext", params.getExts());
        }
        FilterAggregationBuilder exts_limit = AggregationBuilders.
                filter("exts_limit", ext_filter)
                .subAggregation(created_time_ranges)
                .subAggregation(modified_time_ranges);
        
        // ----- Agg2 time_ranges_limit -----
        
        // Agg2.2 results agg
        Page page = new Page(params.getPage(), params.getPer_page());
        TopHitsAggregationBuilder resultsAgg =
                AggregationBuilders.topHits("results")
                        .from(page.getFrom())
                        .size(page.getSize());
        
        // Agg2.1 group_by_ext
        TermsAggregationBuilder group_by_ext = AggregationBuilders
                .terms("group_by_ext").field("ext");
        
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (params.getCreated_time() != null)
            boolQueryBuilder.filter().add(
                    QueryBuilders.rangeQuery("created_time").timeZone(time_zone)
                            .gte(params.getCreated_time().getFrom())
                            .lte(params.getCreated_time().getTo())
            );
        if (params.getModified_time() != null)
            boolQueryBuilder.filter().add(
                    QueryBuilders.rangeQuery("modified_time").timeZone(time_zone)
                            .gte(params.getModified_time().getFrom())
                            .lte(params.getModified_time().getTo())
            );
        // Agg2 time_ranges_limit
        FilterAggregationBuilder time_ranges_limit = AggregationBuilders
                .filter("time_ranges_limit", boolQueryBuilder
                ).subAggregation(group_by_ext)
                .subAggregation(resultsAgg);
        
        searchSourceBuilder.aggregation(exts_limit);
        searchSourceBuilder.aggregation(time_ranges_limit);
    }
    
    private DateRangeAggregationBuilder buildTimeRangesAgg(
            String name,
            String field,
            String time_zone,
            SearchRequestParams.TimeRangeFilter timeRangeFilter,
            Map<String, Integer> timeRangeKey2No
    )
    {
        DateRangeAggregationBuilder dateRangeAgg = new DateRangeAggregationBuilder(name);
        dateRangeAgg.field(field);
        dateRangeAgg.format("yyyy-MM-dd");
        dateRangeAgg.timeZone(ZoneId.of("UTC" + time_zone));
        addDefaultRange(dateRangeAgg);
        if (timeRangeFilter != null)
        {
            String to = timeRangeFilter.getTo() == null ? "now" : timeRangeFilter.getTo();
            dateRangeAgg.addRange("自定义", timeRangeFilter.getFrom(), to);
        }
        
        // 便于后续搜索结果排序
        timeRangeKey2No.clear();
        for (int i = 0; i < dateRangeAgg.ranges().size(); i++)
            timeRangeKey2No.put(dateRangeAgg.ranges().get(i).getKey(), i);
        
        return dateRangeAgg;
    }
    
    // TODO: 更改为读取配置文件
    private static void addDefaultRange(DateRangeAggregationBuilder dateRangeAgg)
    {
        List<RangeAggregator.Range> ranges = Arrays.asList(
                new RangeAggregator.Range("全部", null, "now"),
                new RangeAggregator.Range("三天内", "now-3d/d", null),
                new RangeAggregator.Range("一周内", "now-1w/d", null),
                new RangeAggregator.Range("一个月内", "now-1M/d", null),
                new RangeAggregator.Range("三个月内", "now-3M/d", null),
                new RangeAggregator.Range("半年内", "now-6M/d", null),
                new RangeAggregator.Range("一年内", "now-1y/d", null),
                new RangeAggregator.Range("一年前", null, "now-1y/d")
        );
        for (RangeAggregator.Range range : ranges)
            dateRangeAgg.addRange(range);
    }
}
