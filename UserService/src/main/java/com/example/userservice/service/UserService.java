package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;


    public String createUser(){
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        userRepo.save(user);
        return user.getUsername();
    }

    public User getUserByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public User updateUser(String username, String name, String address, String phone){
        User user = userRepo.findByUsername(username);
        user.setAddress(address);
        user.setPhone(phone);
        user.setName(name);
        userRepo.save(user);
        return user;
    }


}
