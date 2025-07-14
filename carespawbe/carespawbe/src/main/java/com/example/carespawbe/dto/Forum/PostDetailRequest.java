package com.example.carespawbe.dto.Forum;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostDetailRequest {
    public Long postId;
}
