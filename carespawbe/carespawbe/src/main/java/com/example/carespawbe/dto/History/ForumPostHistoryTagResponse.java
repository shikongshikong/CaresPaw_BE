package com.example.carespawbe.dto.History;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ForumPostHistoryTagResponse {
    private Long id;
    private Long userId;
    private String fullname;
    private String title;
    private LocalDate createAt;
    private Long viewedAmount;
    private Long commentedAmount;
    private int isFollowed;
}
