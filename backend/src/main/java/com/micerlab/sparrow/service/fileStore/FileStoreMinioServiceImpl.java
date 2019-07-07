package com.micerlab.sparrow.service.fileStore;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.BusinessException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FileStoreMinioServiceImpl implements FileStoreService {

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
