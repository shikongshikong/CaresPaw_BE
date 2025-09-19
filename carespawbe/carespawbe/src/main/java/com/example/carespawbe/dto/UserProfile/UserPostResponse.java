package com.example.carespawbe.dto.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class UserPostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate createAt;
    private LocalDate updateAt;
    private int state;
    private int typeId;
    private Long viewedAmount;
    private Long commentedAmount;
    private List<Integer> categoryList;
}
