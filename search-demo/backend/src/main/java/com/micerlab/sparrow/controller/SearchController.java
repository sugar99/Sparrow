package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SearchType;
import com.micerlab.sparrow.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SearchController
{
    private SearchService searchService;
    
    public SearchController(SearchService searchService)
    {
        this.searchService = searchService;
    }
    
    @ApiOperation("S1.Search Suggestions")
    @GetMapping("/search/suggestions")
    public Result getSearchSuggestions(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "10") int size)
    {
        SearchType.validateSearchType(type);
        return searchService.getSearchSuggestions(type,keyword,size);
    }
    
    @ApiOperation("S2.Top Associations")
    @GetMapping("/search/associations")
    public Result getTopAssociations(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "5") int category_count,
            @RequestParam(defaultValue = "5") int tag_count
    )
    {
        return searchService.getTopAssociations(keyword, category_count, tag_count);
    }
    
    @ApiOperation("S3.Search Results")
    @PostMapping("/search/results")
    public Result getSearchResults()
    {
        return null;
    }
}
