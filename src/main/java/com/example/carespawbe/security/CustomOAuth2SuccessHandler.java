package com.example.carespawbe.security;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.ShopRepository;
import com.example.carespawbe.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired private UserRepository userRepository;
    @Autowired private JwtService jwtService;

    @Autowired private ObjectMapper objectMapper;

    // Nếu bạn có ShopRepository để lấy shopId theo userId/email thì inject thêm:
    @Autowired(required = false)
    private ShopRepository shopRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name  = oAuth2User.getAttribute("name");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không lấy được email từ tài khoản Google");
            return;
        }

        // Tìm hoặc tạo user
        UserEntity user = userRepository.findByEmail(email).orElseGet(() -> {
            UserEntity u = new UserEntity();
            u.setEmail(email);
            u.setFullname(name);
            u.setRole(1);
            u.setState(1);
            return userRepository.save(u);
        });

        // Tạo token
        String token = jwtService.generateToken(user);

        // Lấy shopId nếu role = 2
        Long shopId = null;
        if (user.getRole() == 2) {
            // Tuỳ schema của bạn:
            // Cách A: UserEntity có field shopId sẵn:
            // shopId = user.getShopId();

            // Cách B: query Shop theo userId/email
            if (shopRepository != null) {
                shopId = shopRepository.findShopIdByUserId(user.getId()).orElse(null);
            }
        }

        // DTO chỉ gồm id, email, role (+ shopId khi role=2)
        UserResponse dto = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                shopId
        );

        String userJson = objectMapper.writeValueAsString(dto);
        String encodedUser = URLEncoder.encode(userJson, StandardCharsets.UTF_8);

        response.sendRedirect("http://localhost:3000/oauth-success?token="
                + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&user=" + encodedUser);
    }

    // shopId nullable: role != 2 thì shopId sẽ null
    public record UserResponse(Long id, String email, Integer role, Long shopId) {}
}
