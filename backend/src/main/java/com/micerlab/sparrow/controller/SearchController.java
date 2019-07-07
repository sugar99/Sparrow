package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SearchType;
import com.micerlab.sparrow.domain.SpaFilterType;
import com.micerlab.sparrow.service.search.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api
@RestController
public class SearchController
{
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
    public Result getSearchResults(@RequestBody Map<String, Object> searchResultParams)
    {
        return searchService.getSearchResults(searchResultParams);
    }
    
    @ApiOperation("S4.搜索类目或标签")
    @GetMapping("/v1/search/{filter_types:(?:tags|categories)}")
    public Result searchSpaFilterTypes(
            @PathVariable String filter_types,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return searchService.searchSpaFilterTypes(spaFilterType, keyword, size);
    }
    
    
}
