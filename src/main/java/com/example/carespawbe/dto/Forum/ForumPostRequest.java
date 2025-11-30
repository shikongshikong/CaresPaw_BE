package com.example.carespawbe.dto.Forum;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ForumPostRequest {
    private String title;
    private String content;
    private int state;
    private int typeId;
    private Long userId;
    private List<Integer> selectedCategoryList;
}
