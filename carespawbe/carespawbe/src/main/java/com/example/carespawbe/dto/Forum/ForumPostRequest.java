package com.example.carespawbe.dto.Forum;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ForumPostRequest {
    private String title;
    private String content;
    private String status;
    private String type;
}
