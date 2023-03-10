package com.bewind.springbootstart.controller;

import com.bewind.springbootstart.model.entity.User;
import com.bewind.springbootstart.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/save")
    public void test(){
        User user = new User();
        user.setUserAccount("test");
        user.setUserPassword("123456");
        userService.save(user);
    }
}
