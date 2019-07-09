package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.CreateSpaFileParams;
import com.micerlab.sparrow.domain.params.UpdateFileMetaParams;
import com.micerlab.sparrow.domain.params.UpdateFileSpaFiltersParams;
import com.micerlab.sparrow.domain.search.SpaFilter;
import com.micerlab.sparrow.domain.search.SpaFilterType;
import com.micerlab.sparrow.domain.*;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.file.FileService;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.utils.FileUtil;
import com.micerlab.sparrow.utils.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileStoreService fileStoreService;

    @Autowired
    private ACLService aclService;

    @ApiOperation("F1.获取policy（阿里云OSS）")
    @PostMapping("/v1/files/policy")
    public Result getPolicy(@RequestBody Map<String, Object> params, HttpServletRequest request){
        String cur_id = params.get("cur_id").toString();
        if (!aclService.hasPermission(BaseService.getUser_Id(request), cur_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        return fileStoreService.getPolicy(params);
    }

    @ApiOperation("F2.获取签名URL（Minio）")
    @PostMapping("/v1/files/url")
    public Result getPresignedUrl(@RequestBody Map<String, Object> params, HttpServletRequest request){
        String cur_id = params.get("cur_id").toString();
        if (!aclService.hasPermission(BaseService.getUser_Id(request), cur_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        return fileStoreService.getPresignedUrl(params);
    }

    @ApiOperation("F6.删除文件")
    @DeleteMapping("/v1/files")
    public Result deleteFile(@RequestBody Map<String, Object> params, HttpServletRequest httpServletRequest){
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可写权限
        // TODO: delete es meta 文件
        return fileStoreService.deleteFile(params);
    }

    @ApiOperation("F7.下载文件")
    @GetMapping("v1/files/{file_id}/download")
    public Result downloadFile(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest){
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可读权限
        return fileStoreService.downloadFile(file_id);
    }

    @ApiOperation("F8.获取文件历史版本列表")
    @GetMapping("/v1/files/{file_id}/versions")
    public Result getFileVersions(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest){
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可读权限
        return fileService.getFileVersions(file_id);
    }
    
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
            @RequestBody CreateSpaFileParams params
            )
    {
        //回调接口不需要认证？
        return fileService.createFileMeta(file_id, params);
    }
    
    @ApiOperation("F9.获取文件Meta")
    @GetMapping("/v1/files/{file_id}")
    public Result retrieveFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id
    )
    {
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可读权限
        return fileService.retrieveFileMeta(file_id);
    }
    
    @ApiOperation("F10.更新文件Meta")
    @PatchMapping("/v1/files/{file_id}")
    public Result updateFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id,
            @RequestBody UpdateFileMetaParams params
            )
    {
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可写权限
        return fileService.updateFileMeta(file_id, params);
    }
    
    
    @ApiOperation("F11.创建类目或标签")
    @PostMapping("/v1/{filter_types:(?:tags|categories)}")
    public Result createSpaFilter(
            @PathVariable String filter_types,
            @RequestBody SpaFilter spaFilter
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService.createSpaFilter(spaFilterType, spaFilter);
    }
    
    
    @ApiOperation("F12.获取类目或标签")
    @GetMapping("/v1/{filter_types:(?:tags|categories)}/{filter_id}")
    public Result retrieveSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService.retrieveSpaFilter(spaFilterType, filter_id);
    }
    
    @ApiOperation("F13. 更新类目或标签")
    @PutMapping("/v1/{filter_types:(?:tags|categories)}/{filter_id}")
    public Result updateSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id,
            @RequestBody SpaFilter spaFilter
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService.updateSpaFilter(spaFilterType, filter_id, spaFilter);
    }
    
    @ApiOperation("F14.删除类目或标签")
    @DeleteMapping("/v1/{filter_types:(?:tags|categories)}/{filter_id}")
    public Result deleteSpaFilter(
            @PathVariable String filter_types,
            @PathVariable String filter_id
    )
    {
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService.deleteSpaFilter(spaFilterType, filter_id);
    }
    
    @ApiOperation("F15.获取文件的类目或标签")
    @GetMapping("/v1/files/{file_id}/{filter_types:(?:tags|categories)}")
    public Result retrieveFileSpaFilters(
            HttpServletRequest request,
            @PathVariable String file_id,
            @PathVariable String filter_types
    )
    {
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可读权限
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        return fileService.retrieveFileSpaFilters(file_id, spaFilterType);
    }
    
    @ApiOperation("F16.更新文件的类目或标签")
    @PutMapping("/v1/files/{file_id}/{filter_types:(?:tags|categories)}")
    public Result updateFileSpaFilters(
            HttpServletRequest request,
            @PathVariable String file_id,
            @PathVariable String filter_types,
            @RequestBody UpdateFileSpaFiltersParams params
            )
    {
        // TODO: 通过file_id获取doc_id (ES)
        // TODO: ACL 判断用户对当前文档是否具有可写权限
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        List<Long> spaFilterIds;
        if(SpaFilterType.TAG == spaFilterType)
            spaFilterIds = params.getTags();
        else spaFilterIds = params.getCategories();
        return fileService.updateFileSpaFilters(file_id, spaFilterType, spaFilterIds);
    }
    
    /**
     * added by chenlvjia
     * 请勿删除
     * @param type 文件类型
     */
    @ApiOperation("测试接口：获取文件类型的拓展名")
    @GetMapping("/v1/files/exts")
    public Result testLoadExts(@RequestParam String type)
    {
        List<String> exts = FileUtil.loadTypeExtsConfig(type);
        return Result.OK().data(exts).build();
    }
}
