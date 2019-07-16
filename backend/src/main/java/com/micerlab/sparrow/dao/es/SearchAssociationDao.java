package com.micerlab.sparrow.dao.es;

import com.micerlab.sparrow.config.ESConfig;
import com.micerlab.sparrow.domain.search.Category;
import com.micerlab.sparrow.domain.search.Tag;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class SearchAssociationDao
{
    private static Logger logger = LoggerFactory.getLogger(SearchAssociationDao.class);
    
    @Autowired
    private ESConfig.Indices sparrowIndices;
    
    @Autowired
    private ESBaseDao ESBaseDao;
    
    /*
    {
      "query": {
        "multi_match": {
          "query": "算法导论",
          "fields": [
            "title",
            "title.cn^3"
          ]
        }
      },
      "aggs": {
        "top_tags": {
          "terms": {
            "field": "tags",
            "size": 5
          }
        },
        "top_categories": {
          "terms": {
            "field": "categories",
            "size": 5
          }
        }
      }
    }
     */
    public Map<String, Object> topAssociations(String keyword, int category_count, int tag_count) throws IOException
    {
        SearchRequest topAssociationsRequest = new SearchRequest(sparrowIndices.getFile());
        
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        query(searchSourceBuilder, keyword);
        aggs(searchSourceBuilder, category_count, tag_count);
        logger.debug("top associations query dsl: {}", searchSourceBuilder.toString());
        
        topAssociationsRequest.source(searchSourceBuilder);
        SearchResponse topAssociationsResponse = ESBaseDao.search(topAssociationsRequest);
        Aggregations aggregations = topAssociationsResponse.getAggregations();
        
        List<String> categoryIds = new LinkedList<>();
        Terms top_categories = aggregations.get("top_categories");
        for (Terms.Bucket bucket : top_categories.getBuckets())
            categoryIds.add(bucket.getKeyAsString());
        
        List<String> tagIds = new LinkedList<>();
        Terms top_tags = aggregations.get("top_tags");
        for (Terms.Bucket bucket : top_tags.getBuckets())
            tagIds.add(bucket.getKeyAsString());
        
        MultiSearchRequest mSearchRequest = new MultiSearchRequest();
        mSearchRequest.add(idsQuery(sparrowIndices.getCategory(), categoryIds.toArray(new String[categoryIds.size()])));
        mSearchRequest.add(idsQuery(sparrowIndices.getTag(), tagIds.toArray(new String[tagIds.size()])));
        MultiSearchResponse mSearchResponse = ESBaseDao.getRestHighLevelClient().msearch(mSearchRequest, RequestOptions.DEFAULT);
        
        MultiSearchResponse.Item categoriesResponse = mSearchResponse.getResponses()[0];
        List<Category> categories = new LinkedList<>();
        if (!categoriesResponse.isFailure())
            for (SearchHit searchHit : categoriesResponse.getResponse().getHits().getHits())
            {
                Map<String, Object> source = searchHit.getSourceAsMap();
                categories.add(new Category(
                        (int) (source.get("id")),
                        source.get("title").toString(),
                        (String) (source.get("desc"))));
            }
        
        MultiSearchResponse.Item tagsResponse = mSearchResponse.getResponses()[1];
        List<Tag> tags = new LinkedList<>();
        if (!tagsResponse.isFailure())
            for (SearchHit searchHit : tagsResponse.getResponse().getHits().getHits())
            {
                Map<String, Object> source = searchHit.getSourceAsMap();
                tags.add(new Tag(
                        (int) (source.get("id")),
                        source.get("title").toString(),
                        (String) (source.get("desc"))));
            }
        
        Map<String, Object> data = new HashMap<>();
        data.put("categories", categories);
        data.put("tags", tags);
        return data;
    }
    
    private static void query(SearchSourceBuilder searchSourceBuilder, String keyword)
    {
        if (StringUtils.isEmpty(keyword))
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        else
        {
            Map<String, Float> fields = new HashMap<>();
            fields.put("title", 1f);
            fields.put("title.cn", 3f);
            searchSourceBuilder.query(
                    QueryBuilders.multiMatchQuery(keyword).fields(fields)
                            .type(MultiMatchQueryBuilder.Type.MOST_FIELDS)
            );
        }
    }
    
    private static void aggs(SearchSourceBuilder searchSourceBuilder, int category_count, int tag_count)
    {
        TermsAggregationBuilder top_categories = AggregationBuilders
                .terms("top_categories").field("categories").size(category_count);
        TermsAggregationBuilder top_tags = AggregationBuilders
                .terms("top_tags").field("tags").size(tag_count);
        searchSourceBuilder.aggregation(top_categories).aggregation(top_tags);
    }
    
    public static SearchRequest idsQuery(String index, String[] ids)
    {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.idsQuery().addIds(ids));
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }
}
