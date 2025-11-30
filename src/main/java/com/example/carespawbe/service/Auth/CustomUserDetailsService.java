//package com.example.carespawbe.service.Auth;
//
//import com.example.carespawbe.dto.Auth.CustomUserDetails;
//import com.example.carespawbe.entity.Auth.UserEntity;
//import com.example.carespawbe.repository.Auth.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
//
//    public UserDetails loadUserByUserId(Long id) throws UsernameNotFoundException {
//        UserEntity userEntity = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("UserEntity not found"));
//        return new CustomUserDetails(userEntity);
//    }
//}


package com.example.carespawbe.service.Auth;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private String getRoleName(int roleCode) {
        return switch (roleCode) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_USER";
            case 2 -> "ROLE_SHOP_OWNER";
            case 3 -> "ROLE_EXPERT";
            default -> "ROLE_USER";
        };
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(() -> getRoleName(user.getRole()))
        );
    }

    public UserDetails loadUserByUserId(Long id) {
        UserEntity user = userRepository.findById(id).orElse(null);
        if (user == null) return null;
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(() -> getRoleName(user.getRole()))
        );
    }
}
