package com.example.carespawbe.dto.Forum;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ForumPostRequest {
    private String title;
    private String content;
    private String state;
    private String type;
    private Long userId;
}
