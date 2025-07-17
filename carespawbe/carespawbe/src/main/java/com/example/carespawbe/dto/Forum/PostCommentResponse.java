package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PostCommentResponse {
    private Long userId;
    private String avatar;
    private String fullname;
    private LocalDate createAt;
    private String content;
}
