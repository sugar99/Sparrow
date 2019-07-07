package com.micerlab.sparrow.service.fileStore;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileStoreMinioServiceImpl implements FileStoreService {

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public Result getPolicy(Map<String, Object> params){
        throw new BusinessException();
    }

    @Override
    public Result getPresignedUrl(Map<String, Object> params){

        return null;
    }

    @Override
    public Result deleteFile(Map<String, Object> params){
        return null;
    }

    @Override
    public Result downloadFile(String file_id){
        return null;
    }
}
