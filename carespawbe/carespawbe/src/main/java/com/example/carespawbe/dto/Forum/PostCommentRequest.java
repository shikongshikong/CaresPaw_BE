package com.example.carespawbe.dto.Forum;

import lombok.Data;

@Data
public class PostCommentRequest {
    private Long postId;
    private String content;
    private Long userId;
}
