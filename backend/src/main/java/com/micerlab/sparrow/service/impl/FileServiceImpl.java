package com.micerlab.sparrow.service.impl;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.FileService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    public Result getPolicy(Map<String, Object> params){
        return null;
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

    public Result getFileVersions(String file_id){
        return null;
    }
}
