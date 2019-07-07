package com.micerlab.sparrow.service.impl;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.ResourceService2;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ResourceServiceImpl2 implements ResourceService2
{
    @Override
    public Result retrieveDocMeta(String doc_id)
    {
        return null;
    }
    
    @Override
    public Result updateDocMeta(String doc_id, Map<String, Object> parms)
    {
        return null;
    }
}
