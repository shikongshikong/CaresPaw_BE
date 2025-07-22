package com.example.carespawbe.dto.History;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class ForumPostSideBarResponse {
    private Long postId;
    private LocalDate createdAt;
    private String title;
    private Long viewedAmount;
    private Long commentedAmount;
    private String avatar;
    private Long userId;
    private String fullname;
    private boolean saved;
}
