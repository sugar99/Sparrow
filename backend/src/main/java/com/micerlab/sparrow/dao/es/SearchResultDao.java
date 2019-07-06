package com.micerlab.sparrow.dao.es;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class SearchResultDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchResultDao.class);
    
    private RestHighLevelClient elasticsearchClient;
    
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
    public Map<String, Object> searchResults(Map<String, Object> searchResultParams) throws IOException
    {
        SearchRequest searchRequest = new SearchRequest(SparrowIndex.SPA_FILES.getIndex());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        
        query(searchSourceBuilder, searchResultParams);
        aggs(searchSourceBuilder, searchResultParams);
        logger.debug("search result query dsl: " + searchSourceBuilder.toString());
        
        searchRequest.source(searchSourceBuilder);
    
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        
        
        return null;
    }
    
    private static void query(SearchSourceBuilder searchSourceBuilder, Map<String, Object> searchResultParams)
    {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        
        
        
        
    }
    
    private static void aggs(SearchSourceBuilder searchSourceBuilder, Map<String, Object> searchResultParams)
    {
    
    }
}
