package com.example.carespawbe.dto.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    private String fullname;
    private int gender;
    private String phoneNum;
//    private String avatar;
    private LocalDate birthday;

}
