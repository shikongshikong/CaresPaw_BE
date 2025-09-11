package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostCategoryRequest {
    private Long postId;
    private int categoryId;
}
