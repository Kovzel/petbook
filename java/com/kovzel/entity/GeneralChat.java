package com.kovzel.entity;

import com.kovzel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class GeneralChat extends Chat{
    @Autowired
    private UserRepository userRepository;

    public GeneralChat(){
        for (User user : userRepository.findAll()){
            users.add(user);
        }
    }
}
