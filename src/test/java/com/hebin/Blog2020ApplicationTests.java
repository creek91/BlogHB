package com.hebin;

import com.hebin.dao.BlogRepository;
import com.hebin.po.Blog;
import com.hebin.po.User;
import com.hebin.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

@SpringBootTest
class Blog2020ApplicationTests {

	@Autowired
	private UserService userService;
	@Autowired
	private BlogRepository blogRepository;

	@Test
	void contextLoads() {
	}

	@Test
	public void testSaveUser(){
		User user=new User();
		user.setUsername("creek");
		user.setPassword("123456");
		user.setAvatar("https://picsum.photos/id/1/100/100?image=1005");
		user.setCreateTime(new Date());
		user.setEmail("123456@qq.com");
		user.setNickname("ss");
		user.setType(1);
		user.setUpdateTime(new Date());
		userService.saveUser(user);
	}

	@Test
	public void testFindByTypeId(){
		Long id=16L;

		List<Blog> blogs=blogRepository.findByTypeId(id);
		for (Blog blog:blogs){
			System.out.println(blog.getType().getId()+blog.getTitle());
		}

	}
}
