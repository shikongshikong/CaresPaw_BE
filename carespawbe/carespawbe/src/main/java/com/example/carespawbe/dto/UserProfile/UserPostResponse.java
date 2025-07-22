package com.example.carespawbe.dto.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UserPostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate createAt;
    private LocalDate updateAt;
    private String state;
    private String type;
    private Long viewedAmount;
    private Long commentedAmount;
}
