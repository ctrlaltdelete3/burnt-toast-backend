package com.example.burnttoast.service;

import com.example.burnttoast.model.User;
import com.example.burnttoast.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

public class BaseService {
    protected final UserRepository userRepository;

    public BaseService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getCurrentlyLoggedUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }
}
