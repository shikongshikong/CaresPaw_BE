package com.example.carespawbe.dto.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private String fullname;
    private int gender;
    private String email;
    private String phoneNumber;
    private String avatar;
    private int role;
    private int state;
    private LocalDate birthday;
}
