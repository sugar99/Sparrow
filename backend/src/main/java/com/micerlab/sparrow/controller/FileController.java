package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.file.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("F1.获取policy（阿里云OSS）")
    @PostMapping("/v1/files/policy")
    public Result getPolicy(@RequestBody Map<String, Object> params, HttpServletRequest httpServletRequest){
        return fileService.getPolicy(params, httpServletRequest);
    }

    @ApiOperation("F2.获取签名URL（Minio）")
    @PostMapping("/v1/files/url")
    public Result getPresignedUrl(@RequestBody Map<String, Object> params, HttpServletRequest httpServletRequest){
        return fileService.getPresignedUrl(params, httpServletRequest);
    }

    @ApiOperation("F6.删除文件")
    @DeleteMapping("/v1/files")
    public Result deleteFile(@RequestBody Map<String, Object> params, HttpServletRequest httpServletRequest){
        return fileService.deleteFile(params, httpServletRequest);
    }

    @ApiOperation("F7.下载文件")
    @GetMapping("v1/files/{file_id}/download")
    public Result downloadFile(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest){
        return fileService.downloadFile(file_id, httpServletRequest);
    }

    @ApiOperation("F8.获取文件历史版本列表")
    @GetMapping("/v1/files/{file_id}/versions")
    public Result getFileVersions(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest){
        return fileService.getFileVersions(file_id, httpServletRequest);
    }
}
