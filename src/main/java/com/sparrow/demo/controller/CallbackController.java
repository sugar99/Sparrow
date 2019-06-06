package com.sparrow.demo.controller;

import com.sparrow.demo.service.callback.CallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author allen
 */
@RestController

public class CallbackController {

    @Autowired
    private CallbackService callbackService;

    @RequestMapping(value = "policy",method = RequestMethod.GET)
    public void doPolicyGet(HttpServletRequest request, HttpServletResponse response){
        callbackService.doPolicyGet(request,response);
    }

    @RequestMapping(value = "callback",method = RequestMethod.POST)
    public void doCallbackPost(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws IOException {
        callbackService.doCallbackPost(params,request,response);
    }


}
