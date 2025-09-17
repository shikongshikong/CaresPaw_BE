package com.example.carespawbe.service;

import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity getUserById(Long id) {
        return userRepository.findUserById(id);
    }
}
