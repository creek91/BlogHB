package com.hebin.service;

import com.hebin.po.Blog;
import com.hebin.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {

    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlogByUser(Long userId, Pageable pageable);

    Page<Blog> listBlogByUser(Long userId, Pageable pageable,BlogQuery blog);

    Page<Blog> listBlog(String  query, Pageable pageable);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    Map<String,List<Blog>> archiveBlog();

    List<Blog> listRecommendBlogTop(Integer size);

    Long countBlogs();

    void deleteBLog(Long id);

}
