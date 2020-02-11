package com.hebin.service.impl;

import com.hebin.NotFoundException;
import com.hebin.dao.BlogRepository;
import com.hebin.dao.CommentRepository;
import com.hebin.po.Blog;
import com.hebin.po.Comment;
import com.hebin.po.Type;
import com.hebin.service.BlogService;
import com.hebin.util.MarkdownUtils;
import com.hebin.util.MyBeanUtils;
import com.hebin.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog=blogRepository.findById(id).orElse(null);
        if (blog==null){
            throw new NotFoundException("博客不存在！");
        }

        Blog b=new Blog();
        BeanUtils.copyProperties(blog,b);
        String content=b.getContent();
        b.setContent( MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join =root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId()==null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.findById(id).orElse(null);
        if (b==null){
            throw new NotFoundException("博客不存在！");
        }
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String > years=blogRepository.findGroupYear();
        Map<String,List<Blog>> map=new LinkedHashMap<>();

        for(String year : years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable= PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Long countBlogs() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public void deleteBLog(Long id) {
        Blog blog=blogRepository.findById(id).orElse(null);
        if (blog==null){
            return;
        }
        List<Comment> comments=blog.getComments();
        for (Comment comment: comments){
            comment.setBlog(null);
            commentRepository.save(comment);
        }

        blogRepository.deleteById(id);
    }
}
