package com.example.carespawbe.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class RegisterRequest {
    private String fullname;
    private String email;
    private String password;
    private String confirmPassword;
    private int gender;
    private LocalDate birthday;

}
