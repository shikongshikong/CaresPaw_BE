package com.example.carespawbe.service;

import com.example.carespawbe.dto.Auth.RegisterRequest;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserEntity login(String email, String password) {
//        String encodedPassword = passwordEncoder.encode(password);
//        System.out.println("Login pass: " + encodedPassword);
//        return userRepository.findByEmailAndPassword(email, encodedPassword).orElse(null);
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isEmpty()) return null;

        String dbPass = user.get().getPassword();
        boolean matched = passwordEncoder.matches(password, dbPass);

        if (matched)
            return user.get();
        else
            return null;
    }

    public UserEntity register(RegisterRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .fullname(request.getFullname())
                .email(request.getEmail())
                .gender(Integer.parseInt(request.getGender()))
                .password(encodedPassword)
                .build();
        return userRepository.save(userEntity);
    }

    public UserEntity checkExistingEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
