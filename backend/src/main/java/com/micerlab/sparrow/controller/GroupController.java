package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.CreateSpaGroupParams;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.group.GroupService;
import com.micerlab.sparrow.utils.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;

    @ApiOperation("G1.新建群组")
    @PostMapping("/v1/groups")
    @ResponseBody
    public Result createGroup(HttpServletRequest request, @RequestBody CreateSpaGroupParams params) {
        return groupService.createGroup(BaseService.getUser_Id(request), params);
    }

    @ApiOperation("G2.获取群组元数据")
    @GetMapping("/v1/groups/{group_id}")
    @ResponseBody
    public Result getGroupMeta(@PathVariable("group_id") String group_id) {
        return groupService.getGroupMeta(group_id);
    }

    @ApiOperation("G3.修改群组元数据")
    @PutMapping("/v1/groups/{group_id}")
    @ResponseBody
    public Result updateGroupMeta(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                  @RequestBody Map<String, Object> paramMap) {
        //判断用户是否为群组的群主
        if (!BaseService.getUser_Id(request).equals(groupService.getGroupOwnerId(group_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_OWNER, "");
        }
        return groupService.updateGroupMeta(group_id, paramMap);
    }

    @ApiOperation("G4.删除群组")
    @DeleteMapping("/v1/groups/{group_id}")
    @ResponseBody
    public Result deleteGroup(HttpServletRequest request, @PathVariable("group_id") String group_id) {
        //判断用户是否为群组的群主
        if (!BaseService.getUser_Id(request).equals(groupService.getGroupOwnerId(group_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_OWNER, "");
        }
        return groupService.deleteGroup(group_id);
    }

    @ApiOperation("G5.添加群组用户")
    @PostMapping("/v1/groups/{group_id}/members")
    @ResponseBody
    public Result addGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                 @RequestBody Map<String, Object> paramMap) {
        //判断用户是否为群组的群主
        if (!BaseService.getUser_Id(request).equals(groupService.getGroupOwnerId(group_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_OWNER, "");
        }
        return groupService.addGroupMember(group_id, paramMap);
    }

    @ApiOperation("G6.获取群组用户")
    @GetMapping("/v1/groups/{group_id}/members")
    @ResponseBody
    public Result getGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id) {
        return groupService.getGroupMember(BaseService.getUser_Id(request), group_id);
    }

    @ApiOperation("G7.删除群组用户")
    @DeleteMapping("/v1/groups/{group_id}/members/{member_id}")
    @ResponseBody
    public Result deleteGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                    @PathVariable("member_id") String member_id) {
        //判断用户是否为群组的群主
        if (!BaseService.getUser_Id(request).equals(groupService.getGroupOwnerId(group_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_OWNER, "");
        }
        return groupService.deleteGroupMember(group_id, member_id);
    }

    @ApiOperation("G8. 获取群组拥有操作权限的资源列表")
    @GetMapping("/v1/groups/{group_id}/authgroups")
    @ResponseBody
    public Result getAuthResources(HttpServletRequest request, @PathVariable("group_id") String group_id) {
        //判断用户是否为群组的成员
        if (!BaseService.getGroupIdList(request).contains(group_id)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_GROUP_MEMBER, "");
        }
        return groupService.getAuthResource(group_id);
    }
}
