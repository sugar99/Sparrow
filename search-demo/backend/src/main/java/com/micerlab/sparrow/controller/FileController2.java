package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.SpaFilterType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.SpaFilter;
import com.micerlab.sparrow.service.FileService2;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class FileController2
{
    @Autowired
    private FileService2 fileService2;
    
    /**
     * 上传文件成功后，前端 / 阿里云oss 调用该接口创建文件Meta
     * @param file_id 文件id
     * @param params 文件Meta部分参数
     */
    @ApiOperation("F5.创建文件Meta")
    @PostMapping("/v1/files/{file_id}")
    public Result createFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id,
            @RequestBody Map<String, Object> params
            )
    {
        // TODO: params中含有 creator, doc_id 字段
        // TODO: ACL 判定该creator是否拥有当前doc_id的写权限
        
        
        return fileService2.createFileMeta(file_id, params);
    }
    
    @ApiOperation("F9.获取文件Meta")
    @GetMapping("/v1/files/{file_id}")
    public Result retrieveFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id
    )
    {
        // TODO: ACL 验证是否拥有该文件的读取权限
        
        return fileService2.retrieveFileMeta(file_id);
    }
    
    @ApiOperation("F10.更新文件Meta")
    @PatchMapping("/v1/files/{file_id}")
    public Result updateFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id,
            @RequestBody Map<String, Object> params
    )
    {
        // TODO: ACL 验证是否拥有该文件的修改权限
        return fileService2.updateFileMeta(file_id, params);
    }
    
    
    @ApiOperation("F11.创建类目或标签")
    @PostMapping("/v1/{filter_types:(tags)|(categories)}")
    public Result createSpaFilter(
            @PathVariable String filter_types,
            @RequestBody SpaFilter spaFilter
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.createSpaFilter(spaFilterType, spaFilter);
    }
    
    
    @ApiOperation("F12.获取类目或标签")
    @GetMapping("/v1/{filter_types:(tags)|(categories)}/{filter_id}")
    public Result retrieveSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.retrieveSpaFilter(spaFilterType, filter_id);
    }
    
    @ApiOperation("F13. 更新类目或标签")
    @PutMapping("/v1/{filter_types:(tags)|(categories)}/{filter_id}")
    public Result updateSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id,
            @RequestBody SpaFilter spaFilter
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.updateSpaFilter(spaFilterType, filter_id, spaFilter);
    }
    
    @ApiOperation("F14.删除类目或标签")
    @DeleteMapping("/v1/{filter_types:(tags)|(categories)}/{filter_id}")
    public Result deleteSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.deleteSpaFilter(spaFilterType, filter_id);
    }
    
    @ApiOperation("F15.获取文件的类目或标签")
    @GetMapping("/v1/files/{file_id}/{filter_types:(tags)|(categories)}")
    public Result retrieveFileSpaFilters(
            HttpServletRequest request,
            @PathVariable String file_id,
            @PathVariable String filter_types
    )
    {
        // TODO: ACL 验证是否拥有该文件的读取权限
        
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.retrieveFileSpaFilters(file_id, spaFilterType);
    }
    
    @ApiOperation("F16.更新文件的类目或标签")
    @PutMapping("/v1/files/{file_id}/{filter_types:(tags)|(categories)}")
    public Result updateFileSpaFilters(
            HttpServletRequest request,
            @PathVariable String file_id,
            @PathVariable String filter_types
    )
    {
        // TODO: ACL 验证是否拥有该文件的修改权限
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService2.updateFileSpaFilters(file_id, spaFilterType);
    }
    
}
