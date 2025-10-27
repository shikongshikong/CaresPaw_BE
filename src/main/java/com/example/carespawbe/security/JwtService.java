//package com.example.carespawbe.security;
//
//import com.example.carespawbe.entity.Auth.UserEntity;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class JwtService {
//
////    String secret = "this-is-a-strong-jwt-secret-key-123";
////    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//
//    private static final String SECRET_KEY = "U1eR9sdU8HdJ3qLkp09sN8vX0Az17Egk";
//    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; //24h
//
//    //    Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
//    SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
////    String base64Key = Encoders.BASE64.encode(key.getEncoded());
//
//    public String generateToken(UserEntity user) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userId", user.getId());
//        claims.put("userRole", user.getRole());
//        claims.put("userState", user.getState());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(String.valueOf(user.getId()))
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public Long extractUserId(String token) {
//        return Long.parseLong(extractAllClaims(token).get("userId").toString());
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
////    public Claims extractClaims(String token, Function<Claims, T> claimsResolver) {
////        final Claims claims = extractAllClaims(token);
////        return claimsResolver.apply(claims);
////    }
//
//
//
////    public boolean isTokenValid(String token, User user) {
////        final Long id = extractUserIdFromToken(token);
////    }
//
//
//
//
//}


package com.example.carespawbe.security;

import com.example.carespawbe.entity.Auth.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String SECRET_KEY = "U1eR9sdU8HdJ3qLkp09sN8vX0Az17Egk";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24h

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    //Tạo JWT token chứa thông tin user (gồm role dạng chuỗi)
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("userState", user.getState());

        // Chuyển role từ số -> chuỗi ROLE_...
        String roleName = switch (user.getRole()) {
            case 0 -> "ROLE_ADMIN";
            case 1 -> "ROLE_USER";
            case 2 -> "ROLE_SHOP_OWNER";
            case 3 -> "ROLE_EXPERT";
            default -> "ROLE_USER";
        };
        claims.put("role", roleName);

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //Trích xuất tất cả claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token) {
        return Long.parseLong(extractAllClaims(token).get("userId").toString());
    }

    public String extractUserRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public Integer extractUserState(String token) {
        Object stateObj = extractAllClaims(token).get("userState");
        if (stateObj instanceof Integer) {
            return (Integer) stateObj;
        } else {
            return Integer.parseInt(stateObj.toString());
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            return false;
        }
    }
}
