package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.ResourceType;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.dir.DirectoryService;
import com.micerlab.sparrow.service.resource.ResourceService;
import com.micerlab.sparrow.utils.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
public class DirectoryController {

    @Autowired
    private ACLService aclService;

    @Autowired
    private DirectoryService directoryService;

    @ApiOperation("新建目录")
    @PostMapping("/v1/dirs")
    @ResponseBody
    public Result createDirectory(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
        String cur_id = paramMap.get("cur_id").toString();
        String user_id = BaseService.getUser_Id(request);
        //判断用户对当前目录是否具有可写权限
        if (!aclService.hasPermission(user_id, cur_id, ResourceType.DIR, BaseService.getGroupIdList(request), ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return directoryService.createDir(user_id, cur_id);
    }

    @ApiOperation("获取目录元数据")
    @GetMapping("/v1/dirs/{dir_id}")
    @ResponseBody
    public Result getDirectoryMeta(HttpServletRequest request, @PathVariable("dir_id") String dir_id) {
        //判断用户对指定目录是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), dir_id, ResourceType.DIR, BaseService.getGroupIdList(request),
                ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DIR, "");
        }
        return directoryService.getDir(dir_id);
    }

    @ApiOperation("更新目录名称")
    @PatchMapping("/v1/dirs/{dir_id}")
    @ResponseBody
    public Result updateDirectoryMeta(HttpServletRequest request, @PathVariable("dir_id") String dir_id,
                                      @RequestBody Map<String, Object> paramMap) {
        String user_id = BaseService.getUser_Id(request);
        //判断用户对指定目录是否具有可写权限
        if (!aclService.hasPermission(user_id, dir_id, ResourceType.DIR, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return directoryService.updateDir(user_id, dir_id, paramMap);
    }

    //TODO 临时
    @ApiOperation("更新目录名称(put)")
    @PutMapping("/v1/dirs/{dir_id}")
    @ResponseBody
    public Result updateDirectoryMeta_1(HttpServletRequest request, @PathVariable("dir_id") String dir_id,
                                      @RequestBody Map<String, Object> paramMap) {
        String user_id = BaseService.getUser_Id(request);
        //判断用户对指定目录是否具有可写权限
        if (!aclService.hasPermission(user_id, dir_id, ResourceType.DIR, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return directoryService.updateDir(user_id, dir_id, paramMap);
    }

    @ApiOperation("删除目录")
    @DeleteMapping("/v1/dirs/{dir_id}")
    @ResponseBody
    public Result deleteDirectory(HttpServletRequest request, @PathVariable("dir_id") String dir_id) {
        //判断用户对指定目录是否具有可写权限
        String user_id = BaseService.getUser_Id(request);
        if (!aclService.hasPermission(user_id, dir_id, ResourceType.DIR, BaseService.getGroupIdList(request),
                ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return directoryService.deleteDir(user_id, dir_id);
    }

    @ApiOperation("获取下级资源")
    @GetMapping("/v1/dirs/{dir_id}/slaves")
    @ResponseBody
    public Result getSlaves(HttpServletRequest request, @PathVariable("dir_id") String dir_id) {
        //判断用户对指定目录是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), dir_id, ResourceType.DIR, BaseService.getGroupIdList(request), ActionType.READ)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_TARGET_RESOURCE, "");
        }
        return directoryService.getSlaveResources(dir_id);
    }

    @ApiOperation("授予群组对指定目录的操作权限")
    @PostMapping("/v1/dirs/{dir_id}/permissions")
    @ResponseBody
    public Result addPermission(HttpServletRequest request, @PathVariable("dir_id") String dir_id,
                                @RequestBody Map<String, Object> paramMap) {
        //判断用户是否为该目录的创建者
        if (!BaseService.getUser_Id(request).equals(directoryService.getCreatorId(dir_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return aclService.addGroupPermission(dir_id, ResourceType.DIR, paramMap);
    }

    @ApiOperation("移除群组对指定目录的操作权限")
    @DeleteMapping("/v1/dirs/{dir_id}/permissions/{group_id}")
    @ResponseBody
    public Result removePermission(HttpServletRequest request, @PathVariable("dir_id") String dir_id,
                                   @PathVariable("group_id") String group_id) {
        //判断用户是否为该目录的创建者
        if (!BaseService.getUser_Id(request).equals(directoryService.getCreatorId(dir_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return aclService.deleteGroupPermission(group_id, dir_id);
    }

    @ApiOperation("获取对该目录有操作权限的群组")
    @GetMapping("/v1/dirs/{dir_id}/authgroups")
    @ResponseBody
    public Result getAuthGroups(HttpServletRequest request, @PathVariable("dir_id") String dir_id) {
        //判断用户对指定目录是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), dir_id, ResourceType.DIR, BaseService.getGroupIdList(request), ActionType.READ)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_TARGET_RESOURCE, "");
        }
        return aclService.getAuthGroups(BaseService.getUser_Id(request), dir_id, "dir");
    }

}
