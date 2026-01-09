package com.example.carespawbe.dto.Forum;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class ForumPostCommentRequest {
    private Long postId;
    private String content;
    private Long userId;
}
