//package com.example.carespawbe.security;
//
//import com.example.carespawbe.entity.User;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    private static final String SECRET_KEY = "secret_key";
//    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;
//
//    public String generateToken(User user) {
//        return generateToken(new HashMap<>(), user);
//    }
//
//    public String generateToken(Map<String, Object> claims, User user) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(String.valueOf(user.getId()))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
//                .compact();
//    }
//
//    public Long extractUserIdFromToken(String token) {
//        return Long.parseLong(extractClaim(token, Claims::getSubject));
//    }
//
//    public boolean isTokenValid(String token, User user) {
//        final Long id = extractUserIdFromToken(token);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//}
