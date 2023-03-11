package com.bewind.springbootstart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bewind.springbootstart.model.dto.user.UserQueryRequest;
import com.bewind.springbootstart.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bewind.springbootstart.model.vo.LoginUserVO;
import com.bewind.springbootstart.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 19824
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2023-03-10 21:35:35
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);
}
