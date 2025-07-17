package com.example.carespawbe.dto.Forum;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDetailResponse {
    private PostResponse post;
    private List<PostCommentResponse> comments;
}
