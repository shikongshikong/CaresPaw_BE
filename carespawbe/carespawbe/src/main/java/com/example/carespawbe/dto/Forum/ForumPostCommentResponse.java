package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ForumPostCommentResponse {
    private Long userId;
    private String avatar;
    private String fullname;
    private String createAt;
    private String content;
    private int followState;
}
