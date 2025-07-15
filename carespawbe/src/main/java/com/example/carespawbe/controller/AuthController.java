package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Auth.LoginRequest;
import com.example.carespawbe.dto.Auth.LoginResponse;
import com.example.carespawbe.dto.Auth.RegisterRequest;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.UserMapper;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3000")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    JwtService jwtService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        User user = authService.login(request.getEmail(), request.getPassword());
        System.out.println("ID receive: " + user.getId());
//        if (user == null) {
//            return null;
//            return ResponseEntity.status(401).body("Invalid username or password");
//        }
//        LoginResponse response = userMapper.toResponse(user);
        String token = jwtService.generateToken(user);
        System.out.println("token in login: " + token);
//        Map<String, String> map = new HashMap<>();
//        map.put("token", token);
//        map.put("id", user.getId().toString());
//        return map;

        return Map.of("token", token);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        LoginResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/check-email")
    public ResponseEntity<?> checkExistingEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user = authService.checkExistingEmail(email);
        if (user == null) {
            return ResponseEntity.ok(Map.of("message", "Valid email."));
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
        }
    }
}
