package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SearchResultParams;
import com.micerlab.sparrow.domain.search.SearchType;
import com.micerlab.sparrow.domain.meta.SpaFilterType;
import com.micerlab.sparrow.service.search.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class SearchController
{
    private Logger logger = LoggerFactory.getLogger(SearchController.class);
    
    @Autowired
    private SearchService searchService;
    
    @ApiOperation("S1.搜索建议")
    @GetMapping("/v1/search/suggestions")
    public Result getSearchSuggestions(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size)
    {
        SearchType.validateSearchType(type);
        return searchService.getSearchSuggestions(type,keyword,size);
    }
    
    @ApiOperation("S2.获取高度相关的类目标签")
    @GetMapping("/v1/search/associations")
    public Result getTopAssociations(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "5") int category_count,
            @RequestParam(defaultValue = "5") int tag_count
    )
    {
        return searchService.getTopAssociations(keyword, category_count, tag_count);
    }
    
    
    @ApiOperation("S3.搜索结果")
    @PostMapping("/v1/search/results")
    public Result getSearchResults(
        @RequestBody SearchResultParams params
    )
    {
//        return Result.OK().data(params).build();
        return searchService.getSearchResults(params);
    }
    
    @ApiOperation("S4.搜索类目或标签")
    @GetMapping("/v1/search/{filter_types:(?:tags|categories)}")
    public Result searchSpaFilters(
            @ApiParam(allowableValues = "tags,categories", required = true)
            @PathVariable  String filter_types,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return searchService.searchSpaFilters(spaFilterType, keyword, size);
    }
    
    @ApiOperation("S5.搜索用户")
    @GetMapping("/v1/search/users")
    public Result searchSpaUser(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size
    )
    {
        return searchService.searchUser(keyword, size);
    }

    @ApiOperation("S6.搜索群组")
    @GetMapping("/v1/search/groups")
    public Result searchSpaGroup(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size
    )
    {
        return searchService.searchGroup(keyword, size);
    }
}
