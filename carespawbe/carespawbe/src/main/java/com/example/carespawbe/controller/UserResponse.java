package com.example.carespawbe.controller;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String fullname, email, avatar, role;
}
