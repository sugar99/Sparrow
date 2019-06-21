package com.sparrow.demo.controller;

import com.sparrow.demo.service.file.FileService;
import com.sparrow.demo.utils.ResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author allen
 */
@RestController
@RequestMapping("/user")
public class FileController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/file/upload",method = RequestMethod.POST)
    public ResultModel uploadFile(@RequestBody Map<String, Object> params){
        return fileService.uploadFile(params);
    }

    @RequestMapping(value = "/file/delete",method = RequestMethod.POST)
    public ResultModel deleteFile(@RequestBody Map<String, Object> params){
        return fileService.deleteFile(params);
    }

    @RequestMapping(value = "/file/download",method = RequestMethod.POST)
    public void downloadFile(@RequestBody Map<String, Object> requestMap, HttpServletResponse response){
        fileService.downloadFile(requestMap,response);
    }

    @RequestMapping(value = "/file/getUrl",method = RequestMethod.POST)
    public ResultModel getFileUrl(@RequestBody Map<String, Object> params){
        return fileService.getFileUrl(params);
    }

    @RequestMapping(value = "/file/version",method = RequestMethod.POST)
    public ResultModel getFileVersion(@RequestBody Map<String, Object> params){
        return fileService.getFileVersion(params);
    }
}
