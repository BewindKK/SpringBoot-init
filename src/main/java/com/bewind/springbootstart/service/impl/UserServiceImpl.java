package com.bewind.springbootstart.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewind.springbootstart.model.entity.User;
import com.bewind.springbootstart.service.UserService;
import com.bewind.springbootstart.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 19824
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-03-10 21:35:35
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




