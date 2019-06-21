package com.sparrow.demo.controller;

import com.sparrow.demo.service.document.DocumentService;
import com.sparrow.demo.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author allen
 */
@RestController
@RequestMapping("/user")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping(value = "/doc/add",method = RequestMethod.POST)
    public ResultModel addDoc(){
        return documentService.addDoc();
    }

    @RequestMapping(value = "/doc/meta/update",method = RequestMethod.POST)
    public ResultModel updateDocMeta(@RequestBody Map<String, Object> requestMap){
        return documentService.updateDocMeta(requestMap);
    }

    @RequestMapping(value = "/doc/list",method = RequestMethod.POST)
    public ResultModel getDocList(@RequestBody Map<String, Object> requestMap){
        return documentService.getDocList(requestMap);
    }

    @RequestMapping(value = "/doc/detail",method = RequestMethod.POST)
    public ResultModel getDocDetail(@RequestBody Map<String, Object> requestMap){
        return documentService.getDocDetail(requestMap);
    }

    @RequestMapping(value = "/doc/delete",method = RequestMethod.POST)
    public ResultModel deleteDoc(@RequestBody Map<String, Object> requestMap){
        return documentService.deleteDoc(requestMap);
    }

    @RequestMapping(value = "/doc/download",method = RequestMethod.POST)
    public void downloadDoc(@RequestBody Map<String, Object> requestMap, HttpServletResponse response){
        documentService.downloadDoc(requestMap,response);
    }
}
