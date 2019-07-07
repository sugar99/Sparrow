package com.micerlab.sparrow.controller;

import com.micerlab.sparrow.domain.Result;
import com.micerlab.sparrow.service.base.BaseService;
import com.micerlab.sparrow.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Api
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @ApiOperation("U1.新建用户")
    @PostMapping("/users")
    @ResponseBody
    public Result createUser(@RequestBody Map<String, Object> paramMap) {
        return userService.createUser(paramMap);
    }

    @ApiOperation("U2.用户登录")
    @PostMapping("/login")
    @ResponseBody
    public Result userLogin(HttpServletResponse response, @RequestBody Map<String, Object> paramMap) {
        return userService.userLogin(response, paramMap);
    }

    @ApiOperation("U3.获取用户meta")
    @GetMapping("/users/{user_id}")
    @ResponseBody
    public Result getUserMeta(HttpServletRequest request, @PathVariable("user_id") String user_id) {
        if (user_id.equals("current")) {
            return userService.getUserMeta(BaseService.getUser_Id(request));
        } else {
            return userService.getUserMeta(user_id);
        }
    }

    @ApiOperation("U4.注销登录")
    @PostMapping("/users/logout")
    @ResponseBody
    public Result userLogout(HttpServletRequest request) {
        return userService.userLogout(BaseService.getUser_Id(request));
    }

    @ApiOperation("U5.获取用户所在群组")
    @GetMapping("/users/{user_id}/groups")
    @ResponseBody
    public Result getUserGroups(HttpServletRequest request, @PathVariable("user_id") String user_id) {
        if (user_id.equals("current")) {
            return userService.getUserGroups(BaseService.getUser_Id(request));
        } else {
            return userService.getUserGroups(user_id);
        }
    }
}
