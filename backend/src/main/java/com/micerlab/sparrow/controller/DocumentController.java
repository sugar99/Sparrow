package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.ActionType;
import com.micerlab.sparrow.domain.ErrorCode;
import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.domain.params.SpaDocUpdateParams;
import com.micerlab.sparrow.service.acl.ACLService;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.doc.DocumentService;
import com.micerlab.sparrow.service.resource.ResourceService;
import com.micerlab.sparrow.utils.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
public class DocumentController {
    @Autowired
    private ResourceService resourceService;

    @Autowired
    private ACLService aclService;

    @Autowired
    private DocumentService documentService;

    @ApiOperation("新建文档")
    @PostMapping("/v1/docs")
    @ResponseBody
    public Result createDocument(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
        String cur_id = paramMap.get("cur_id").toString();
        String user_id = BaseService.getUser_Id(request);
        //判断用户对当前目录是否具有可写权限
        if (!aclService.hasPermission(user_id, cur_id, BaseService.getGroupIdList(request), ActionType.WRITE)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return documentService.createDoc(user_id, cur_id);
    }

    @ApiOperation("获取文档元数据")
    @GetMapping("/v1/docs/{doc_id}")
    @ResponseBody
    public Result getDocMeta(HttpServletRequest request, @PathVariable("doc_id") String doc_id) {
        //判断用户对指定文档是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), doc_id, BaseService.getGroupIdList(request),
                ActionType.READ)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_CUR_DIR, "");
        }
        return documentService.getDoc(doc_id);
    }

    @ApiOperation("更新文档元数据")
    @PatchMapping("/v1/docs/{doc_id}")
    @ResponseBody
    public Result updateDocMeta(HttpServletRequest request, @PathVariable("doc_id") String doc_id,
                                @RequestBody SpaDocUpdateParams paramMap) {
        //判断用户对指定文档是否有可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), doc_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return documentService.updateDoc(doc_id, paramMap);
    }

    @ApiOperation("删除文档")
    @DeleteMapping("/v1/docs/{doc_id}")
    @ResponseBody
    public Result deleteDocument(HttpServletRequest request, @PathVariable("doc_id") String doc_id) {
        //判断用户对指定文档是否可写权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), doc_id, BaseService.getGroupIdList(request),
                ActionType.WRITE)){
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_WRITE_CUR_DIR, "");
        }
        return documentService.deleteDoc(doc_id);
    }

    @ApiOperation("获取文档内的文件")
    @GetMapping("/v1/docs/{doc_id}/slaves")
    @ResponseBody
    public Result getSlaves(HttpServletRequest request, @PathVariable("doc_id") String doc_id) {
        //判断用户对指定文档是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), doc_id, BaseService.getGroupIdList(request), ActionType.READ)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_TARGET_RESOURCE, "");
        }
        return documentService.getSlaveFiles(doc_id);
    }

    @ApiOperation("授予群组对指定文档的操作权限")
    @PostMapping("/v1/docs/{doc_id}/permissions")
    @ResponseBody
    public Result addPermission(HttpServletRequest request, @PathVariable("doc_id") String doc_id,
                                @RequestBody Map<String, Object> paramMap) {
        //判断用户是否为文档的创建者
        if (!BaseService.getUser_Id(request).equals(documentService.getCreatorId(doc_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return aclService.addGroupPermission(doc_id, paramMap);
    }

    @ApiOperation("移除群组对指定文档的操作权限")
    @DeleteMapping("/v1/docs/{doc_id}/permissions/{group_id}")
    @ResponseBody
    public Result removePermission(HttpServletRequest request, @PathVariable("doc_id") String doc_id,
                                   @PathVariable("group_id") String group_id) {
        //判断用户是否为文档的创建者
        if (!BaseService.getUser_Id(request).equals(documentService.getCreatorId(doc_id))) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NOT_RESOURCE_OWNER, "");
        }
        return aclService.deleteGroupPermission(group_id, doc_id);
    }

    @ApiOperation("获取对该文档有操作权限的群组")
    @GetMapping("/v1/docs/{doc_id}/authgroups")
    @ResponseBody
    public Result getAuthGroups(HttpServletRequest request, @PathVariable("doc_id") String doc_id) {
        //判断用户对指定文档是否有可读权限
        if (!aclService.hasPermission(BaseService.getUser_Id(request), doc_id, BaseService.getGroupIdList(request), ActionType.READ)) {
            throw new BusinessException(ErrorCode.FORBIDDEN_NO_READ_TARGET_RESOURCE, "");
        }
        return aclService.getAuthGroups(BaseService.getUser_Id(request), doc_id, "doc");
    }

}
