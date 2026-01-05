//package com.example.carespawbe.controller.Auth;
//
//import com.example.carespawbe.dto.Auth.LoginRequest;
//import com.example.carespawbe.dto.Auth.LoginResponse;
//import com.example.carespawbe.dto.Auth.RegisterRequest;
//import com.example.carespawbe.entity.Auth.UserEntity;
//import com.example.carespawbe.mapper.Auth.UserMapper;
//import com.example.carespawbe.security.JwtService;
//import com.example.carespawbe.service.Auth.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/auth")
////@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "*")
//public class AuthController {
//
//    @Autowired
//    private AuthService authService;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @Autowired
//    JwtService jwtService;
//
//    @PostMapping("/login")
//    public Map<String, String> login(@RequestBody LoginRequest request) {
//        UserEntity userEntity = authService.login(request.getEmail(), request.getPassword());
//        System.out.println("ID receive: " + userEntity.getId());
////        if (userEntity == null) {
////            return null;
////            return ResponseEntity.status(401).body("Invalid username or password");
////        }
////        LoginResponse response = userMapper.toResponse(userEntity);
//        String token = jwtService.generateToken(userEntity);
//        System.out.println("token in login: " + token);
////        Map<String, String> map = new HashMap<>();
////        map.put("token", token);
////        map.put("id", userEntity.getId().toString());
////        return map;
//
//        return Map.of("token", token);
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        UserEntity userEntity = authService.register(request);
//        LoginResponse response = userMapper.toResponse(userEntity);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/check-email")
//    public ResponseEntity<?> checkExistingEmail(@RequestBody Map<String, String> request) {
//        String email = request.get("email");
//        UserEntity userEntity = authService.checkExistingEmail(email);
//        if (userEntity == null) {
//            return ResponseEntity.ok(Map.of("message", "Valid email."));
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email already exists"));
//        }
//    }
//}


package com.example.carespawbe.controller.Auth;

import com.example.carespawbe.dto.Auth.LoginRequest;
import com.example.carespawbe.dto.Auth.LoginResponse;
import com.example.carespawbe.dto.Auth.RegisterRequest;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.mapper.Auth.UserMapper;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.Auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtService jwtService;

    //Đăng nhập: sinh JWT và lưu vào cookie
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        UserEntity userEntity = authService.login(request.getEmail(), request.getPassword());

        if (userEntity == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        // Tạo JWT chứa id, role, state
        String token = jwtService.generateToken(userEntity);
        System.out.println("Token generated: " + token);

        // Lưu token vào cookie HTTPOnly
//        Cookie cookie = new Cookie("jwt", token);
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setMaxAge(24 * 60 * 60); // 1 ngày
//        response.addCookie(cookie);

        // Trả lại dữ liệu cho frontend
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "userId", userEntity.getId(),
                "role", userEntity.getRole(),
                "email", userEntity.getEmail(),
                "token", token
        ));

    }

    //Đăng ký người dùng
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        UserEntity userEntity = authService.register(request);
        LoginResponse response = userMapper.toResponse(userEntity);
        return ResponseEntity.ok(response);
    }

    //Kiểm tra email đã tồn tại
    @PostMapping("/check-email")
    public ResponseEntity<?> checkExistingEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        UserEntity userEntity = authService.checkExistingEmail(email);

        if (userEntity == null) {
            return ResponseEntity.ok(Map.of("message", "Valid email."));
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Email already exists"));
        }
    }

    // Đăng xuất (FE tự xoá token)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }
}
