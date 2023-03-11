package com.bewind.springbootstart.controller;

import com.bewind.springbootstart.common.R;
import com.bewind.springbootstart.model.entity.User;
import com.bewind.springbootstart.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/save")
    public R test(){
        User user = new User();
        user.setUserAccount("test");
        user.setUserPassword("123456");
        userService.save(user);
        return R.success();
    }
}
