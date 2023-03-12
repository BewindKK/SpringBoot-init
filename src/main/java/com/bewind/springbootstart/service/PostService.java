package com.bewind.springbootstart.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bewind.springbootstart.model.dto.post.PostQueryRequest;
import com.bewind.springbootstart.model.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bewind.springbootstart.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 19824
* @description 针对表【post(帖子)】的数据库操作Service
* @createDate 2023-03-12 16:43:07
*/
public interface PostService extends IService<Post> {

    void validPost(Post post, boolean b);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    PostVO getPostVO(Post post, HttpServletRequest request);

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request);
}
