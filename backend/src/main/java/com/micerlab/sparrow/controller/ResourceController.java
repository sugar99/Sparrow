package com.micerlab.sparrow.controller;

import com.alibaba.fastjson.JSONObject;
import com.micerlab.sparrow.dao.postgre.ResourceDao;
import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.resource.ResourceService;
import com.micerlab.sparrow.utils.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//即将弃用
@Api
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ACLService aclService;

    @Autowired
    private ResourceDao resourceDao;

    @ApiOperation("D1.新建资源")
    @PostMapping("/v1/resources/{type}")
    @ResponseBody
    public Result createResource(HttpServletRequest request, @PathVariable("type") String type, @RequestBody Map<String, Object> paramMap) {
        String cur_id = paramMap.get("cur_id").toString();
        String user_id = BaseService.getUser_Id(request);
        //判断用户对当前目录是否具有可写权限
        if (!aclService.hasPermission(user_id, cur_id, BaseService.getGroupIdList(request), ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return resourceService.createResource(user_id, type, cur_id);
    }

    @ApiOperation("D2.获取资源元数据")
    @GetMapping("/v1/resources/{resource_id}")
    @ResponseBody
    public Result getResourceMeta(HttpServletRequest request, @PathVariable("resource_id") String resource_id) {
        String cur_id = resourceService.getMasterDirId(resource_id);
        //判断用户对当前目录是否具有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), cur_id, BaseService.getGroupIdList(request),
                ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DIR, "");
        }
        String type = resourceDao.getResourceMeta(resource_id).getResource_type();
        if (type.equals("doc")) {
            return resourceService.retrieveDocMeta(resource_id);
        } else {
            return resourceService.getDirMeta(resource_id);
        }
    }

    @ApiOperation("D3.修改资源元数据")
    @PutMapping("/v1/resources/{resource_id}")
    @ResponseBody
    public Result updateResourceMeta(HttpServletRequest request, @PathVariable("resource_id") String resource_id,
                                     @RequestBody Map<String, Object> paramMap) {
        String cur_id = resourceService.getMasterDirId(resource_id);
        //判断用户对当前目录是否具有可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), cur_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        String type = resourceDao.getResourceMeta(resource_id).getResource_type();
        if (type.equals("doc")) {
            SpaDocUpdateParams params = (new JSONObject(paramMap)).toJavaObject(SpaDocUpdateParams.class);
            return resourceService.updateDocMeta(resource_id, params);
        } else {
            return resourceService.updateDirMeta(resource_id, paramMap);
        }
    }

    @ApiOperation("D4.删除资源")
    @DeleteMapping("/v1/resources/{resource_id}")
    @ResponseBody
    public Result deleteResource(HttpServletRequest request, @PathVariable("resource_id") String resource_id) {
        String cur_id = resourceService.getMasterDirId(resource_id);
        //判断用户对当前目录是否具有可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), cur_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        String type = resourceDao.getResourceMeta(resource_id).getResource_type();
        return resourceService.deleteResource(resource_id, type);
    }

    @ApiOperation("D5.获取下级资源或文件")
    @GetMapping("/v1/resources/{resource_id}/slaves")
    @ResponseBody
    public Result getSlavesResource(HttpServletRequest request, @PathVariable("resource_id") String resource_id) {
        String user_id = BaseService.getUser_Id(request);
        //判断用户对指定资源是否具有可读权限
        if (!aclService.hasPermission(user_id, resource_id, BaseService.getGroupIdList(request), ActionType.READ)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_TARGET_RESOURCE, "");
        }
        String type = resourceDao.getResourceMeta(resource_id).getResource_type();
        return resourceService.getSlavesResource(user_id, resource_id, type);
    }
    
    @ApiOperation("D7.获取文档Meta")
    @GetMapping("/v1/docs/{doc_id}")
    public Result retrieveDocMeta(
            HttpServletRequest request,
            @PathVariable String doc_id
    )
    {
        // TODO: ACL 鉴定是否拥有该 doc_id 的读取权限
        
        return resourceService.retrieveDocMeta(doc_id);
    }

    @ApiOperation("D8.更新文档Meta")
    @PatchMapping("/v1/docs/{doc_id}")
    public Result updateDocMeta(
            HttpServletRequest request,
            @PathVariable String doc_id,
            @RequestBody SpaDocUpdateParams params
            )
    {
        // TODO: ACL 鉴定是否拥有该 doc_id 的修改权限

        return resourceService.updateDocMeta(doc_id, params);
    }
    
    @ApiOperation("F1.授予群组对指定目录或文档的操作权限")
    @PostMapping("/v1/resources/{resource_id}/permissions")
    @ResponseBody
    public Result addPermission(HttpServletRequest request, @PathVariable("resource_id") String resource_id,
                                @RequestBody Map<String, Object> paramMap) {
        String user_id = BaseService.getUser_Id(request);
        String creator_id = resourceService.getCreatorId(resource_id);
        //判断用户是否为资源的创建者
        if (!user_id.equals(creator_id)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return resourceService.addPermission(resource_id, paramMap);
    }
    
    @ApiOperation("F2.移除群组对指定目录或文档的操作权限")
    @DeleteMapping("/v1/resources/{resource_id}/permissions")
    @ResponseBody
    public Result removePermission(HttpServletRequest request, @PathVariable("resource_id") String resource_id,
                                   @RequestBody Map<String, Object> paramMap) {
        String user_id = BaseService.getUser_Id(request);
        String creator_id = resourceService.getCreatorId(resource_id);
        //判断用户是否为资源的创建者
        if (!user_id.equals(creator_id)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return resourceService.removePermission(resource_id, paramMap);
    }
    
    @ApiOperation("F3.获取对该资源有操作权限的群组")
    @GetMapping("/v1/resources/{resource_id}/authgroups")
    @ResponseBody
    public Result getAuthGroups(HttpServletRequest request, @PathVariable("resource_id") String resource_id) {
        return resourceService.getAuthGroups(BaseService.getUser_Id(request), resource_id);
    }
}
