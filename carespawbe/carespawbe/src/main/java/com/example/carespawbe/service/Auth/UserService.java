package com.example.carespawbe.service.Auth;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
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
