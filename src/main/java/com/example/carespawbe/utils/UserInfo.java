package com.example.carespawbe.utils;

import jakarta.servlet.http.HttpServletRequest;

public class UserInfo {
    public static String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}
