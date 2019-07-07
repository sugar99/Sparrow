package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.group.GroupService;
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
    @PostMapping("/groups")
    @ResponseBody
    public Result createGroup(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
        return groupService.createGroup(BaseService.getUser_Id(request), paramMap);
    }

    @ApiOperation("G2.获取群组元数据")
    @GetMapping("/groups/{group_id}")
    @ResponseBody
    public Result getGroupMeta(HttpServletRequest request, @PathVariable("group_id") String group_id) {
        return groupService.getGroupMeta(group_id);
    }

    @ApiOperation("G3.修改群组元数据")
    @PutMapping("/groups/{group_id}")
    @ResponseBody
    public Result updateGroupMeta(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                  @RequestBody Map<String, Object> paramMap) {
        //TODO ACL 只有群主才可以修改群组信息
        return groupService.updateGroupMeta(group_id, paramMap);
    }

    @ApiOperation("G4.删除群组")
    @DeleteMapping("/groups/{group_id}")
    @ResponseBody
    public Result deleteGroup(HttpServletRequest request, @PathVariable("group_id") String group_id) {
        //TODO ACL 只有群主才可以删除群组
        return groupService.deleteGroup(group_id);
    }

    @ApiOperation("G5.添加群组用户")
    @PostMapping("/groups/{group_id}/members")
    @ResponseBody
    public Result addGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id, @RequestBody Map<String, Object> paramMap) {
        //TODO ACL 只有群主才可以添加群成员
        return groupService.addGroupMember(group_id, paramMap);
    }

    @ApiOperation("G6.获取群组用户")
    @GetMapping("/groups/{group_id}/members")
    @ResponseBody
    public Result getGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                 @RequestBody Map<String, Object> paramMap) {
        return groupService.addGroupMember(group_id, paramMap);
    }

    @ApiOperation("G7.删除群组用户")
    @DeleteMapping("/groups/{group_id}/members/{member_id}")
    @ResponseBody
    public Result deleteGroupMember(HttpServletRequest request, @PathVariable("group_id") String group_id,
                                    @PathVariable("member_id") String member_id) {
        //TODO ACL 只有群主才可以删除群成员
        return groupService.deleteGroupMember(group_id, member_id);
    }
}
