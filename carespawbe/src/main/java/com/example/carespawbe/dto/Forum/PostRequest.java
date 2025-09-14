package com.example.carespawbe.dto.Forum;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PostRequest {
    private String title;
    private String content;
    private String state;
    private String type;
    private Long userId;
    private List<Integer> selectedCategories;
}
