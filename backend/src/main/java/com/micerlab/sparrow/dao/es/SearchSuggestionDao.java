package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.meta.FileType;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class SearchSuggestionDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchSuggestionDao.class);
    
    @Autowired
    private ESConfig.Indices sparrowIndices;
    
    @Autowired
    private ESBaseDao ESBaseDao;
    
    /*
    {
      "query": {
        "bool": {
          "must": {
            "multi_match": {
              "query": "算法",
              "fields": [
                "title",
                "title.cn^3"
              ]
            }
          },
          "filter": {
            "script": {
              "script": {
                "source": "
                    !doc.containsKey('type')
                    || doc.containsKey('type')
                    && doc['type'].value.equals(params.type)",
                "params": {
                  "type": "image"
                }
              }
            }
          }
        }
      },
      "aggs": {
        "group_by_title": {
          "terms": {
            "field": "title.raw",
            "order": {
              "term_score.value": "desc"
            }
          },
          "aggs": {
            "term_score": {
              "max": {
                "script": {
                  "source": "_score"
                }
              }
            }
          }
        }
      }
    }
     */
    
    public List<String> suggestions(String type, String keyword, int size)
    {
        SearchRequest searchRequest = new SearchRequest(
                sparrowIndices.getFile(),
                sparrowIndices.getCategory(),
                sparrowIndices.getTag()
        );
        
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);
        query(searchSourceBuilder, keyword, type);
        aggs(searchSourceBuilder, size);
        
        logger.debug("search suggestions query dsl");
        logger.debug(searchSourceBuilder.toString());
        
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = ESBaseDao.search(searchRequest);
        Terms group_by_title = searchResponse.getAggregations().get("group_by_title");
        
        List<String> suggestions = new LinkedList<>();
        for(Terms.Bucket bucket: group_by_title.getBuckets())
            suggestions.add(bucket.getKeyAsString());
        return suggestions;
    }
    
    private static void query(SearchSourceBuilder searchSourceBuilder, String keyword, String type)
    {
        boolean empty_keyword = StringUtils.isEmpty(keyword);
        boolean all_type = FileType.ALL.getType().equals(type);
        if (empty_keyword && all_type)
        {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
            return;
        }
    
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        if (!empty_keyword)
        {
            Map<String, Float> fields = new HashMap<>();
            fields.put("title", 1f);
            fields.put("title.cn", 3f);
            boolQueryBuilder.must(
                    QueryBuilders.multiMatchQuery(keyword).fields(fields)
                            .type(MultiMatchQueryBuilder.Type.MOST_FIELDS)
            );
        }
        if (!all_type)
        {
            Script filterScript = new Script(Script.DEFAULT_SCRIPT_TYPE, Script.DEFAULT_SCRIPT_LANG,
                    "!doc.containsKey('type') || doc.containsKey('type') && doc['type'].value.equals(params.type)",
                    Collections.singletonMap("type", type));
            boolQueryBuilder.filter(QueryBuilders.scriptQuery(filterScript));
        }
        searchSourceBuilder.query(boolQueryBuilder);
    }
    
    /*
    */
    
    /**
     * @param searchSourceBuilder
     * @param size
     */
    private static void aggs(SearchSourceBuilder searchSourceBuilder, int size)
    {
        TermsAggregationBuilder aggs = AggregationBuilders
                .terms("group_by_title").field("title.raw")
                .size(size)
                .order(
                        BucketOrder.aggregation("terms_score", "value", false)
                )
                .subAggregation(
                        AggregationBuilders.max("terms_score")
                                .script(new Script("_score"))
                );
        searchSourceBuilder.aggregation(aggs);
    }
}
