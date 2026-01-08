package com.example.carespawbe.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId(){
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(a.getPrincipal().toString());
    }
}
