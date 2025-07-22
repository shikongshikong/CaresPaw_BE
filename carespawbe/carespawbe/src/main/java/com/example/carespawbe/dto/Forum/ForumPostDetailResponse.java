package com.example.carespawbe.dto.Forum;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ForumPostDetailResponse {
    private ForumPostResponse post;
    private List<ForumPostCommentResponse> comments;
}
