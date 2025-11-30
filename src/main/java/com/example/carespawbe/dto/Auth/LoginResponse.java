package com.example.carespawbe.dto.Auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
//    must have same name with entity to make mapper work correctly!
//    if name is not match, declare clearly by @Mapping(source = "fullName", target = "name")
    private Long id;
    private String fullname;
    private String email;
    private String avatar;
    private String role;

}