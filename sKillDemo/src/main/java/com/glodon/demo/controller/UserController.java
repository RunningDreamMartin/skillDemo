package com.glodon.demo.controller;

import com.glodon.demo.domain.User;

import com.glodon.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@CrossOrigin(origins="*",maxAge=3600)
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    //用户注册
    @ResponseBody
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public Boolean register(@RequestBody User user){
        if (userService.insert(user)) {
            return true ;
        }
        return false;
    }

    //用户登录页面
    @ResponseBody
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestParam("name") String name, @RequestParam("password") String password) {
        return userService.login(name, password);
    }





}
