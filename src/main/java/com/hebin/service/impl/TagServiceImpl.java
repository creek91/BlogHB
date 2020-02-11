package com.hebin.service.impl;

import com.hebin.NotFoundException;
import com.hebin.dao.BlogRepository;
import com.hebin.dao.TagRepository;
import com.hebin.po.Blog;
import com.hebin.po.Tag;
import com.hebin.service.BlogService;
import com.hebin.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private BlogRepository blogRepository;


    @Transactional
    @Override
    public Tag saveTag(Tag tag){
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTag(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort=Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable=PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    @Override
    public List<Tag> listTag(String ids) { //1,2,3
//        return tagRepository.findAll(convertToList(ids));
        return tagRepository.findAllById(convertToList(ids));
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t=tagRepository.findById(id).orElse(null);
        if (t==null){
            throw new NotFoundException("不存在该类");
        }
        BeanUtils.copyProperties(tag,t);
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        Tag tag=tagRepository.findById(id).orElse(null);
        if (tag==null){
            return;
        }
        //通过jpa查询出所有的含有tag的blog，将blog的tags列表remove tagID为要删除id的tag。
//        List<Blog> blogs=tagRepository.findBlogByTagId(id);
//        tag.setBlogs(null);
        tagRepository.deleteById(id);
    }
}
