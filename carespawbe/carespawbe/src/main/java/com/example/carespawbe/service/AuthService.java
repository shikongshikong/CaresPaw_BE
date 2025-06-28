package com.example.carespawbe.service;

import com.example.carespawbe.dto.RegisterRequest;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }

    public User register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .gender(request.getGender())
                .password(encodedPassword)
//                .password(request.getPassword())
                .build();
        return userRepository.save(user);
    }

    public User checkExistingEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
