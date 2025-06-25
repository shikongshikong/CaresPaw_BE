package com.example.carespawbe.controller;

import com.example.carespawbe.dto.LoginRequest;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.UserMapper;
import com.example.carespawbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());

        if (user == null) return ResponseEntity.status(401).body("Invalid username or password");

        UserResponse reponse = userMapper.toResponse(user);
        return ResponseEntity.ok(reponse);
    }
}
