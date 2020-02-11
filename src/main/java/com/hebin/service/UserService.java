package com.hebin.service;

import com.hebin.po.User;

public interface UserService {

    User checkUser(String username ,String password);

    User checkUser(String username);

    void saveUser(User user);

    User getUser(Long id);
}
