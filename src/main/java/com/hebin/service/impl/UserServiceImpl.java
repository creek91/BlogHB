package com.hebin.service.impl;

import com.hebin.dao.UserRepository;
import com.hebin.po.User;
import com.hebin.service.UserService;
import com.hebin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user= userRepository.findByUsernameAndPassword(username, MD5Utils.md5Code(password));
        return user;
    }

    @Override
    public User checkUser(String username) {
        User user= userRepository.findByUsername(username);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }


}
