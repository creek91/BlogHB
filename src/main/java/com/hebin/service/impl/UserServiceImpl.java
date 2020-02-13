package com.hebin.service.impl;

import com.hebin.dao.UserRepository;
import com.hebin.po.User;
import com.hebin.service.UserService;
import com.hebin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, MD5Utils.md5Code(password));
        return user;
    }

    @Override
    public User checkUser(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void createOrUpdateUser(User user1) {

        Long accountId=user1.getAccountId();

        List<User> users = userRepository.findByAccountId(accountId);


        if (users.size()>0){
            User user= users.get(0);
            if (user1.getEmail()!=null){
                user.setEmail(user1.getEmail());
            }
            user.setAvatar(user1.getAvatar());
            user.setUsername(user1.getUsername());
            user.setAccountFrom("GitHub");
            user.setAccountId(user1.getId());

            user.setUpdateTime(new Date());
            userRepository.save(user);
        }else {

            user1.setCreateTime(new Date());
            user1.setUpdateTime(new Date());

            userRepository.save(user1);
        }


    }


}
