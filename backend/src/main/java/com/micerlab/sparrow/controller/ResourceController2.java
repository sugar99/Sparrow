package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.ResourceService2;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ResourceController2
{
    @Autowired
    private ResourceService2 resourceService2;
    
    @ApiOperation("D7.获取文档Meta")
    @GetMapping("/v1/docs/{doc_id}")
    public Result retrieveDocMeta(
            HttpServletRequest request,
        @PathVariable String doc_id
    )
    {
        // TODO: ACL 鉴定是否拥有该 doc_id 的读取权限
        
        return resourceService2.retrieveDocMeta(doc_id);
    }
    
    @ApiOperation("D8.更新文档Meta")
    @PatchMapping("/v1/docs/{doc_id}")
    public Result updateDocMeta(
            HttpServletRequest request,
            @PathVariable String doc_id,
            @RequestBody Map<String, Object> parms
    )
    {
        // TODO: ACL 鉴定是否拥有该 doc_id 的修改权限
        
        return resourceService2.updateDocMeta(doc_id, parms);
    }
}
