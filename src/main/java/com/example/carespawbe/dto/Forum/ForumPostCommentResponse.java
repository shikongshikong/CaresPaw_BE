package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ForumPostCommentResponse {
    private Long userId;
    private String avatar;
    private String fullname;
    private LocalDate createAt;
    private String content;
    private int followState;
}
