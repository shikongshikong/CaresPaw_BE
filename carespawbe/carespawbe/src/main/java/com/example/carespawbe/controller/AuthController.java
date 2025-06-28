package com.example.carespawbe.controller;

import com.example.carespawbe.dto.LoginRequest;
import com.example.carespawbe.dto.LoginResponse;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.UserMapper;
import com.example.carespawbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        System.out.println("Before login");
        System.out.println("receive: " + request.getEmail());
        User user = authService.login(request.getEmail(), request.getPassword());
        if (user == null) return ResponseEntity.status(401).body("Invalid username or password");
        System.out.println("Name: " + user.getFullname());
        LoginResponse response = userMapper.toResponse(user);
        System.out.println("Response: " + response.getFullname());
        return ResponseEntity.ok(response);
    }
}
