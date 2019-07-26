package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.dao.es.SpaDocDao;
import com.micerlab.sparrow.dao.es.SpaFileDao;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.meta.SpaDoc;
import com.micerlab.sparrow.domain.meta.SpaFile;
import com.micerlab.sparrow.domain.params.CreateSpaFileParams;
import com.micerlab.sparrow.domain.params.UpdateFileMetaParams;
import com.micerlab.sparrow.domain.params.UpdateFileSpaFiltersParams;
import com.micerlab.sparrow.domain.meta.SpaFilter;
import com.micerlab.sparrow.domain.meta.SpaFilterType;
import com.micerlab.sparrow.domain.*;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.file.FileService;
import com.micerlab.sparrow.service.fileStore.FileStoreMinioServiceImpl;
import com.micerlab.sparrow.service.fileStore.FileStoreService;
import com.micerlab.sparrow.service.user.UserService;
import com.micerlab.sparrow.utils.FileUtil;
import com.micerlab.sparrow.utils.BusinessException;
import com.micerlab.sparrow.utils.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;

import java.util.List;
import java.util.Map;

@Api
@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    @Qualifier("minioService")
    private FileStoreService fileStoreService;

    @Autowired
    private ACLService aclService;

    

    @Autowired
    private FileStoreMinioServiceImpl fileStoreMinioService;

    @Autowired
    private SpaFileDao spaFileDao;
    
    @Autowired
    private SpaDocDao spaDocDao;

    @Autowired
    private UserService userService;

    @ApiOperation("F1.获取policy（阿里云OSS）")
    @PostMapping("/v1/files/policy")
    public Result getPolicy(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        //判断用户对当前文档是否有可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), params.get("cur_id").toString(), ResourceType.DOC, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        return fileStoreService.getPolicy(params, httpServletRequest, httpServletResponse);
    }

    @ApiOperation("F2.获取签名URL（Minio）")
    @PostMapping("/v1/files/url")
    public Result getPresignedUrl(@RequestBody Map<String, Object> params, HttpServletRequest request){
        //判断用户对当前文档是否有可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), params.get("cur_id").toString(), ResourceType.DOC, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        return fileStoreService.getPresignedUrl(params, request);
    }

    @ApiOperation("F6.删除文件")
    @DeleteMapping("/v1/files")
    public Result deleteFile(@RequestBody Map<String, Object> params, HttpServletRequest httpServletRequest){
        List<String> file_ids = (List<String>) params.get("file_id");
        if(file_ids == null || file_ids.isEmpty()){
//            throw new BusinessException(ErrorCode.BAD_REQUEST_COMMON, "file_id不能为空");
            return Result.OK().build();
        }

        List<String> keys = new LinkedList<>();
        List<String> forbiddenFileIds = new LinkedList<>();

        for (String file_id: file_ids) {
            SpaFile file = spaFileDao.get(file_id);
            SpaDoc doc = spaDocDao.get(file.getDoc_id());

            if(!aclService.hasPermission(BaseService.getUser_Id(httpServletRequest), doc.getId(), ResourceType.DOC, BaseService.getGroupIdList(httpServletRequest), ActionType.WRITE)){
                forbiddenFileIds.add(file_id);
                continue;
            }

            keys.add(file.getStore_key());
            spaFileDao.delete(file_id);

            doc.getFiles().remove(file_id);
            doc.setModified_time(TimeUtil.formatTimeStr(TimeUtil.currentTime()));
            spaDocDao.update(doc.getId(), doc);
        }
        fileStoreService.deleteFile(keys);
        if(!forbiddenFileIds.isEmpty())
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "无法删除文件（没有权限）：" + forbiddenFileIds.toString());
        return Result.OK().build();
    }

    @ApiOperation("F7.下载文件")
    @GetMapping("v1/files/{file_id}/download")
    public Result downloadFile(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        SpaFile fileMeta = spaFileDao.get(file_id);
        String doc_id = fileMeta.getDoc_id();
        //判断用户对当前文档是否有可读权限
        if(!aclService.hasPermission(BaseService.getUser_Id(httpServletRequest), doc_id, ResourceType.DOC, BaseService.getGroupIdList(httpServletRequest), ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DOC, "");
        }
        String title = fileMeta.getTitle();
        String key = fileMeta.getStore_key();
        String ext = fileMeta.getExt();
//        fileStoreService.downloadFile(title + "." + ext, key,httpServletRequest, httpServletResponse);
        return fileStoreMinioService.getFileUrl(key, title + "." + ext);
    }

    @ApiOperation("F8.获取文件历史版本列表(待完成)")
    @GetMapping("/v1/files/{file_id}/versions")
    public Result getFileVersions(@PathVariable("file_id") String file_id, HttpServletRequest httpServletRequest){
        String doc_id = spaFileDao.get(file_id).getDoc_id();
        //判断用户对当前文档是否有可读权限
        if(!aclService.hasPermission(BaseService.getUser_Id(httpServletRequest), doc_id, ResourceType.DOC, BaseService.getGroupIdList(httpServletRequest), ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DOC, "");
        }
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
        String creator = params.getCreator();
        String doc_id = params.getDoc_id();

        //  前端creator字段作废
        String user_id = BaseService.getUser_Id(request);
        params.setCreator(user_id);

        if(!aclService.hasPermission(user_id, doc_id, ResourceType.DOC, BaseService.getGroupIdList(request), ActionType.WRITE)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        return fileService.createFileMeta(file_id, params);
    }
    
    @ApiOperation("F9.获取文件Meta")
    @GetMapping("/v1/files/{file_id}")
    public Result getFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id
    )
    {
        // 判断用户对当前文档是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), spaFileDao.get(file_id).getDoc_id(),ResourceType.DOC,
                BaseService.getGroupIdList(request), ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DOC, "");
        }
        return fileService.getFileMeta(file_id);
    }
    
    @ApiOperation("F10.更新文件Meta")
    @PatchMapping("/v1/files/{file_id}")
    public Result updateFileMeta(
            HttpServletRequest request,
            @PathVariable("file_id") String file_id,
            @RequestBody UpdateFileMetaParams params
            )
    {
        if (!aclService.hasPermission(BaseService.getUser_Id(request), spaFileDao.get(file_id).getDoc_id(),ResourceType.DOC,
                BaseService.getGroupIdList(request), ActionType.WRITE)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
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
        if (!aclService.hasPermission(BaseService.getUser_Id(request), spaFileDao.get(file_id).getDoc_id(),ResourceType.DOC,
                BaseService.getGroupIdList(request), ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DOC, "");
        }
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
        if (!aclService.hasPermission(BaseService.getUser_Id(request), spaFileDao.get(file_id).getDoc_id(),ResourceType.DOC,
                BaseService.getGroupIdList(request), ActionType.WRITE)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DOC, "");
        }
        SpaFilterType spaFilterType = SpaFilterType.fromTypes(filter_types);
        List<Long> spaFilterIds;
        if(SpaFilterType.TAG == spaFilterType)
            spaFilterIds = params.getTags();
        else spaFilterIds = params.getCategories();
        return fileService.updateFileSpaFilters(file_id, spaFilterType, spaFilterIds);
    }
    
    /**
     * added by chenlvjia
     * 测试是否正确访问到文件拓展名配置文件，请勿删除
     * @param type 文件类型
     */
    @ApiOperation("测试接口：获取文件类型的拓展名（勿删）")
    @GetMapping("/v1/files/exts")
    public Result testLoadExts(@RequestParam String type)
    {
        List<String> exts = FileUtil.loadTypeExtsConfig(type);
        return Result.OK().data(exts).build();
    }
}
