package com.micerlab.sparrow.domain.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class SearchRequestParams
{
    private String type = "all";
    
    @JsonIgnore
    private SearchType searchType = SearchType.ALL;
    
    private String keyword;
    
    private List<Long> tags = new ArrayList<>();
    
    private List<Long> categories = new ArrayList<>();
    
    private List<String> exts = Arrays.asList("all");
    
    private TimeRangeFilter created_time;
    
    private TimeRangeFilter modified_time;
    
    private String time_zone = "+8";
    
    private int page = 1;
    
    private int per_page = 10;
    
    public void setType(String type)
    {
        this.type = type;
        this.searchType = SearchType.fromType(type);
    }
    
    @Setter
    @Getter
    public static class TimeRangeFilter
    {
        private String from;
        private String to = "now";
    
        public String toJSONString()
        {
            String json  = "{";
            json += "\"to\":"
                    + (StringUtils.isEmpty(to) ? "now" : to);
            if(!StringUtils.isEmpty(from))
                json += ",\"from\":" + from;
             json += "}";
            return json;
        }
    
        @Override
        public String toString()
        {
            return toJSONString();
        }
    }
    
}
