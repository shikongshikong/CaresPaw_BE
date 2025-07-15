package com.example.carespawbe.security;

import com.example.carespawbe.service.CustomUserDetailsService;
import com.example.carespawbe.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService detailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");

//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        String token = authHeader.substring(7);
//        Long id = jwtService.extractUserId(token);
//
//        if (id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//        }
        Long userId = 0L;
        try {
//            String jwt = getJwtFromRequest(request);
//            if (StringUtils.hasText(jwt) && jwtService.validateToken(jwt)) {
//                Long userId = jwtService.extractUserId(jwt);
//                UserDetails userDetails = detailService.loadUserByUserId(userId);
//                if (userDetails != null) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
            String jwt = getJwtFromRequest(request);
            System.out.println("jwt authentication: " + jwt);
            if (jwt != null) {
                userId = jwtService.extractUserId(jwt);
            }
            request.setAttribute("userId", userId);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JWT authentication failed");
            request.setAttribute("userId", 0L);
        }
        filterChain.doFilter(request, response);
    }

    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//        return bearerToken.substring(7);
        if (bearerToken != null) {
            if (bearerToken.length() > 7) {
                return  bearerToken.substring(7);
            }
        }
        return null;
    }
}
