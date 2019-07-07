package com.micerlab.sparrow.service.impl;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.FileStoreService;
import com.micerlab.sparrow.utils.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileStoreMinioService implements FileStoreService {

    public Result getPolicy(Map<String, Object> params){
        throw new BusinessException();
    }

    public Result getPresignedUrl(Map<String, Object> params){
        return null;
    }

    public Result deleteFile(Map<String, Object> params){
        return null;
    }

    public Result downloadFile(String file_id){
        return null;
    }
}
