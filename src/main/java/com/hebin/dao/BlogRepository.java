package com.hebin.dao;

import com.hebin.po.Blog;
import com.hebin.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long>,JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.recommend=true ")
    List<Blog> findTop(Pageable pageable);

    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query,Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id=?1")
    int updateViews(Long id);

    //查询所有博客的年份，并按照年份分组，各组间降序排列。
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by year order by year desc ")
    List<String> findGroupYear();

    //按照年份查询所有的该年份博客列表
    @Query("SELECT b from Blog b where function('date_format',b.updateTime,'%Y') =?1")
    List<Blog> findByYear(String year);


    @Query("select b from Blog b where b.type.id like ?1")
    List<Blog> findByTypeId(Long id);

}
