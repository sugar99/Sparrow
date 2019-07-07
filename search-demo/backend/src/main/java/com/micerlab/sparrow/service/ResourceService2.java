package com.micerlab.sparrow.service;

import com.micerlab.sparrow.domain.Result;

import java.util.Map;

public interface ResourceService2
{
    Result retrieveDocMeta(String doc_id);
    
    Result updateDocMeta(String doc_id, Map<String, Object> parms);
}
