package com.micerlab.sparrow.service.file;

import com.micerlab.sparrow.domain.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class FileService {

    @Autowired
    private FileServiceImpl fileServiceImpl;

    public Result getPolicy(Map<String, Object> params, HttpServletRequest httpServletRequest){
        // TODO: ACL(httpServletRequest)

        return Result.OK().data(fileServiceImpl.getPolicy(params)).build();
    }

    public Result getPresignedUrl(Map<String, Object> params, HttpServletRequest httpServletRequest){
        // TODO: ACL(httpServletRequest)

        return Result.OK().data(fileServiceImpl.getPresignedUrl(params)).build();
    }

    public Result deleteFile(Map<String, Object> params, HttpServletRequest httpServletRequest){
        // TODO: ACL(httpServletRequest)

        // TODO: delete es meta

        return Result.OK().data(fileServiceImpl.deleteFile(params)).build();
    }

    public Result downloadFile(String file_id, HttpServletRequest httpServletRequest){
        // TODO: ACL(httpServletRequest)

        return Result.OK().data(fileServiceImpl.downloadFile(file_id)).build();
    }

    public Result getFileVersions(String file_id, HttpServletRequest httpServletRequest){
        // TODO: ACL(httpServletRequest)

        return Result.OK().data(fileServiceImpl.getFileVersions(file_id)).build();
    }
}
