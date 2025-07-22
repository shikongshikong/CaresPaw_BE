package com.example.carespawbe.dto.Forum;

import lombok.Data;

@Data
public class ForumPostCommentRequest {
    private Long postId;
    private String content;
    private Long userId;
}
