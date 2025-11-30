package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForumPostDetailRequest {
    private Long postId;
    private Long userId;
}
