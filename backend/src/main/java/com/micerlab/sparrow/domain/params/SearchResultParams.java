package com.micerlab.sparrow.domain.params;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.micerlab.sparrow.domain.file.FileType;
import com.micerlab.sparrow.domain.search.SearchType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 搜索结果API参数
 * @author chenlvjia
 */
@ApiModel
@Getter
@Setter
public class SearchResultParams
{
    @ApiModelProperty(
            allowableValues = "all,image,doc,video,audio,others,doc_content",
            example = "all",
            position = 1
    )
    private String type = "all";
    
    @JsonIgnore
    private SearchType searchType = SearchType.ALL;
    
    @ApiModelProperty(example = "算法", position = 2)
    private String keyword = "";
    
    @ApiModelProperty(example = "[]", position = 3)
    private List<Long> tags = new ArrayList<>();
    
    @ApiModelProperty(example = "[]", position = 4)
    private List<Long> categories = new ArrayList<>();
    
    @ApiModelProperty(example = "[\"all\"]",  position = 5)
    private List<String> exts = Arrays.asList("all");
    
    @ApiModelProperty(position = 6)
    private TimeRangeFilter created_time;
    
    @ApiModelProperty(position = 7)
    private TimeRangeFilter modified_time;
    
    @ApiModelProperty(hidden = true)
    private String time_zone = "+8";
    
    @ApiModelProperty(example = "1", position = 8)
    private int page = 1;
    
    @ApiModelProperty(example = "5", position = 9)
    private int per_page = 10;
    
    @ApiModelProperty(example = "2", position = 10)
    private int desc_highlight_count = 2;
    
    @ApiModelProperty(example = "5", position = 11)
    private int content_highlight_count = 5;
    
    @ApiModelProperty(example = "<em>", position = 12)
    private String highlight_pre_tag = "<em>";
    
    @ApiModelProperty(example = "</em>", position = 13)
    private String highlight_post_tag = "</em>";
    
    public void setType(String type)
    {
        this.type = type;
        this.searchType = SearchType.fromType(type);
    }
    
    @Setter
    @Getter
    public static class TimeRangeFilter
    {
        @ApiModelProperty(example = "now-3d")
        private String from;
        
        @ApiModelProperty(example = "now")
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
