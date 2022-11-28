package com.nordeus.dataengineering.service;


import com.nordeus.dataengineering.model.UserModel;
import com.nordeus.dataengineering.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserModel findUserById(String id){
        return userRepository.findById(id).orElse(null);
    }
}
