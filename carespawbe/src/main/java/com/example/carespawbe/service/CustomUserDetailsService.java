//package com.example.carespawbe.service;
//
//import com.example.carespawbe.dto.Auth.CustomUserDetails;
//import com.example.carespawbe.entity.User;
//import com.example.carespawbe.repository.UserRepository;
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
//        User user = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        return new CustomUserDetails(user);
//    }
//}
