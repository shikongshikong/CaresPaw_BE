package com.example.carespawbe.service;

import com.example.carespawbe.entity.User;
import com.example.carespawbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }
}
