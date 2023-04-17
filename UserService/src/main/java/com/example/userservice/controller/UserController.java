package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.AttributeOverride;
import java.util.UUID;

@Service
public class UserController {
    @Autowired
    UserService userService;

    public String createUser(){
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        userService.save(user);
        return user.getUsername();
    }

    public User getUserByUsername(String username){
        return userService.getUserByUsername(username);
    }

    public User updateUser(String username, String name, String address, String phone){
        User user = userService.getUserByUsername(username);
        user.setAddress(address);
        user.setPhone(phone);
        user.setName(name);
        userService.save(user);
        return user;
    }
}
