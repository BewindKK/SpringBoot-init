package com.bewind.springbootstart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bewind.springbootstart.common.ApiCode;
import com.bewind.springbootstart.constant.CommonConstant;
import com.bewind.springbootstart.exception.ApiException;
import com.bewind.springbootstart.model.dto.user.UserQueryRequest;
import com.bewind.springbootstart.model.entity.User;
import com.bewind.springbootstart.model.vo.LoginUserVO;
import com.bewind.springbootstart.model.vo.UserVO;
import com.bewind.springbootstart.service.UserService;
import com.bewind.springbootstart.mapper.UserMapper;
import com.bewind.springbootstart.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bewind.springbootstart.constant.CommonConstant.SALT;
import static com.bewind.springbootstart.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author 19824
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2023-03-10 21:35:35
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new ApiException(ApiCode.PARAM_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new ApiException(ApiCode.PARAM_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new ApiException(ApiCode.PARAM_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new ApiException(ApiCode.PARAM_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new ApiException(ApiCode.PARAM_ERROR, "账号重复");
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new ApiException(ApiCode.VALIDATE_FAILED,"注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new ApiException(ApiCode.PARAM_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new ApiException(ApiCode.PARAM_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new ApiException(ApiCode.PARAM_ERROR, "密码错误");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", userAccount);
        queryWrapper.eq("user_password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new ApiException(ApiCode.PARAM_ERROR, "用户不存在或密码错误");
        }
        // 3. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new ApiException(ApiCode.VALIDATE_FAILED,"未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new ApiException(ApiCode.VALIDATE_FAILED,"未登录");
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new ApiException(ApiCode.VALIDATE_FAILED,"未登录");
        }
        return currentUser;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new ApiException( ApiCode.VALIDATE_FAILED,"请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public void validUser(User user, boolean add) {
        if (user == null) {
            throw new ApiException(ApiCode.VALIDATE_FAILED);
        }
        String userAccount = user.getUserAccount();
        String userPassword = user.getUserPassword();
        String userName = user.getUserName();

        // 创建时，参数不能为空
        if (add) {
            if (StringUtils.isAnyBlank(userAccount, userPassword)){
                throw new ApiException(ApiCode.VALIDATE_FAILED,"用户账户或密码为空");
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(userAccount) && userAccount.length() < 4) {
            throw new ApiException(ApiCode.VALIDATE_FAILED, "用户账户长度过短");
        }
        if (StringUtils.isNotBlank(userPassword) && userPassword.length() < 8) {
            throw new ApiException(ApiCode.VALIDATE_FAILED, "用户密码过短");
        }
        if (StringUtils.isNotBlank(userName) && userName.length() > 20) {
            throw new ApiException(ApiCode.VALIDATE_FAILED, "用户名过长");
        }
    }
}




