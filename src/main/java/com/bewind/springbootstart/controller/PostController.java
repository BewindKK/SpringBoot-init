package com.bewind.springbootstart.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewind.springbootstart.annotation.AuthCheck;
import com.bewind.springbootstart.common.ApiCode;
import com.bewind.springbootstart.common.DeleteRequest;
import com.bewind.springbootstart.common.R;
import com.bewind.springbootstart.constant.UserConstant;
import com.bewind.springbootstart.exception.ApiException;
import com.bewind.springbootstart.model.dto.post.PostAddRequest;
import com.bewind.springbootstart.model.dto.post.PostQueryRequest;
import com.bewind.springbootstart.model.dto.post.PostUpdateRequest;
import com.bewind.springbootstart.model.entity.Post;
import com.bewind.springbootstart.model.entity.User;
import com.bewind.springbootstart.model.vo.PostVO;
import com.bewind.springbootstart.service.PostService;
import com.bewind.springbootstart.service.UserService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {
    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 创建
     *
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public R<Long> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request) {
        if (postAddRequest == null) {
            throw new ApiException(ApiCode.VALIDATE_FAILED);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postAddRequest, post);
        List<String> tags = postAddRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        postService.validPost(post, true);
        User loginUser = userService.getLoginUser(request);
        post.setUserId(loginUser.getId());
        post.setFavourNum(0);
        post.setThumbNum(0);
        boolean result = postService.save(post);
        if (!result){
            throw new ApiException(ApiCode.VALIDATE_FAILED,"文章添加失败");
        }
        long newPostId = post.getId();
        return R.success(newPostId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public R<Boolean> deletePost(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new ApiException(ApiCode.VALIDATE_FAILED);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        if (oldPost==null){
            throw new ApiException(ApiCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new ApiException(ApiCode.NO_AUTH_ERROR);
        }
        boolean b = postService.removeById(id);
        return R.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param postUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public R<Boolean> updatePost(@RequestBody PostUpdateRequest postUpdateRequest) {
        if (postUpdateRequest == null || postUpdateRequest.getId() <= 0) {
            throw new ApiException(ApiCode.VALIDATE_FAILED);
        }
        Post post = new Post();
        BeanUtils.copyProperties(postUpdateRequest, post);
        List<String> tags = postUpdateRequest.getTags();
        if (tags != null) {
            post.setTags(GSON.toJson(tags));
        }
        // 参数校验
        postService.validPost(post, false);
        long id = postUpdateRequest.getId();
        // 判断是否存在
        Post oldPost = postService.getById(id);
        if (oldPost==null){
            throw new ApiException(ApiCode.NOT_FOUND_ERROR);
        }
        boolean result = postService.updateById(post);
        return R.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public R<PostVO> getPostVOById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new ApiException(ApiCode.VALIDATE_FAILED);
        }
        Post post = postService.getById(id);
        if (post == null) {
            throw new ApiException(ApiCode.NOT_FOUND_ERROR);
        }
        return R.success(postService.getPostVO(post, request));
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public R<Page<PostVO>> listPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                       HttpServletRequest request) {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        if (size>20){
            throw new ApiException(ApiCode.PARAM_ERROR);
        }
        Page<Post> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return R.success(postService.getPostVOPage(postPage, request));
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param postQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    public R<Page<PostVO>> listMyPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                         HttpServletRequest request) {
        if (postQueryRequest == null) {
            throw new ApiException(ApiCode.PARAM_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postQueryRequest.setUserId(loginUser.getId());
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        if (size>20){
            throw new ApiException(ApiCode.PARAM_ERROR);
        }
        Page<Post> postPage = postService.page(new Page<>(current, size),
                postService.getQueryWrapper(postQueryRequest));
        return R.success(postService.getPostVOPage(postPage, request));
    }

    // endregion
}
