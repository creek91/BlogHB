package com.hebin.dao;

import com.hebin.po.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);
    User findByUsername(String username);
    List<User> findByAccountId(Long accountId);
}
