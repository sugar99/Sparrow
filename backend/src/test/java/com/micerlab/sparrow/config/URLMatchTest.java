package com.micerlab.sparrow.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.regex.Pattern;

public class URLMatchTest
{
    @Test
    public void test1()
    {
        PathMatcher pathMatcher = new AntPathMatcher();
        String pattern = "/v{:\\d+}/users/**";
        
        String url1 = "/v1/users";
        String url2 = "/v1/users/";
        String url3 = "/v1/users/1";
    
         Assert.assertTrue(pathMatcher.match(pattern,url1));
        Assert.assertTrue(pathMatcher.match(pattern,url2));
        Assert.assertTrue(pathMatcher.match(pattern,url3));
        
    }
    
    @Test
    public void test2()
    {
        Pattern pattern = Pattern.compile("/v\\d+/users(/.+)?");
        
        String url1 = "/v1/users";
        String url2 = "/v1/users/";
        String url3 = "/v1/users/1";
    
        Assert.assertTrue(pattern.matcher(url1).matches());
        Assert.assertFalse(pattern.matcher(url2).matches());
        Assert.assertTrue(pattern.matcher(url3).matches());
    }
}
