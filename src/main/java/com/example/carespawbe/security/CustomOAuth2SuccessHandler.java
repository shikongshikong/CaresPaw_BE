package com.example.carespawbe.security;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        LocalDate birthday = oAuth2User.getAttribute("birthday");

        if (email == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Không lấy được email từ tài khoản Google");
            return;
        }

        //Kiểm tra xem user đã tồn tại chưa
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        UserEntity user;

        if (optionalUser.isEmpty()) {
            //Tạo mới user nếu chưa có
            user = new UserEntity();
            user.setEmail(email);
            user.setFullname(name);
            user.setBirthday(birthday);
            user.setRole(1);   // tuỳ theo cấu trúc của bạn
            user.setState(1);  // active
            userRepository.save(user);
            System.out.println("Tạo mới user từ Google: " + email);
        } else {
            user = optionalUser.get();
            System.out.println("User đã tồn tại: " + email);
        }

        //Tạo JWT token
        String token = jwtService.generateToken(user);

        //Gửi token về FE
        response.sendRedirect("http://localhost:3000/oauth-success?token=" + token);
    }
}
