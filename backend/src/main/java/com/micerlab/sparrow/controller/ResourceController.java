package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.resource.ResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api
@RestController
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @ApiOperation("D1.新建资源")
    @PostMapping("/v1/resources/{type}")
    @ResponseBody
    public Result createResource(@PathVariable("type") String type, @RequestBody Map<String, Object> paramMap) {
        String cur_id = paramMap.get("cur_id").toString();
        //TODO ACL 只有对当前目录有可写权限才可以进行该操作
        return resourceService.createResource(type, cur_id);
    }

    @ApiOperation("D2.获取资源元数据")
    @GetMapping("/v1/resources/{resource_id}")
    @ResponseBody
    public Result getResourceMeta(@PathVariable("resource_id") String resource_id) {
        //TODO ACL 只有对该资源有可读权限的用户才可以进行该操作
        return resourceService.getResourceMeta(resource_id);
    }

    @ApiOperation("D3.修改资源元数据")
    @PutMapping("/v1/resource/{resource_id}")
    @ResponseBody
    public Result updateResourceMeta(@PathVariable("resource_id") String resource_id,
                                     @RequestBody Map<String, Object> paramMap) {
        //TODO ACL 只有对该资源有可写权限的用户才可以进行该操作
        return resourceService.updateResourceMeta(resource_id, paramMap);
    }

    @ApiOperation("D4.删除资源")
    @DeleteMapping("/v1/resource/{resource_id}")
    @ResponseBody
    public Result deleteResource(@PathVariable("resource_id") String resource_id) {
        //TODO ACL 只有对该资源有可写权限的用户才可以进行该操作
        return resourceService.deleteResource(resource_id);
    }

    @ApiOperation("D5.获取下级资源或文件")
    @GetMapping("/v1/resources/{resource_id}/slaves")
    @ResponseBody
    public Result getSlavesResource(@PathVariable("resource_id") String resource_id) {
        //TODO ACL 只有对该资源有可读权限的用户才可以进行该操作
        return resourceService.getSlavesResource(resource_id);
    }

    @ApiOperation("D6.获取对该资源有操作权限的群组")
    @GetMapping("/v1/resources/{resource_id}/authgroups")
    @ResponseBody
    public Result getAuthGroups(@PathVariable("resource_id") String resource_id) {
        return resourceService.getAuthGroups(resource_id);
    }

    @ApiOperation("D7.授予群组对指定目录或文档的操作权限")
    @PostMapping("/v1/resources/{resource_id}/permissions")
    @ResponseBody
    public Result addPermission(@PathVariable("resource_id") String resource_id,
                                @RequestBody Map<String, Object> paramMap) {
        //TODO ACL 只有资源的创建者有权进行该操作
        return resourceService.addPermission(resource_id, paramMap);
    }

    @ApiOperation("D8.移除群组对指定目录或文档的操作权限")
    @DeleteMapping("/v1/resources/{resource_id}/permissions")
    @ResponseBody
    public Result removePermission(@PathVariable("resource_id") String resource_id,
                                   @RequestBody Map<String, Object> paramMap) {
        //TODO ACL 只有资源的创建者有权进行该操作
        return resourceService.removePermission(resource_id, paramMap);
    }
}
