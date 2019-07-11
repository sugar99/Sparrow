package com.micerlab.sparrow.config;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Arrays;
import java.util.List;

public class AccessManager {
    private static PathMatcher matcher = new AntPathMatcher();

    private static List<String> authenticateUriList = Arrays.asList(
            "/v1/users/**",
            "/v1/groups/**",
            "/v1/resources/**",
            "/v1/files/**",
            "/v1/dirs/**",
            "/v1/docs/**",
            "/v1/users",
            "/v1/groups",
            "/v1/resources",
            "/v1/files",
            "/v1/dirs",
            "/v1/docs"
            
    );

    public static boolean mathUriList(String uri, List<String> uriList) {
        for (String pattern: uriList) {
            if (matcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }

    public static boolean mathAuthenticateUriList(String uri) {
        return mathUriList(uri, authenticateUriList);
    }
}
