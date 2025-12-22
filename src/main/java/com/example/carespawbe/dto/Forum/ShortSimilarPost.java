package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortSimilarPost {
    private Long id;
    private Long userId;
    private String fullname;
    private String title;
    private Long viewedAmount;
    private Long commentedAmount;
}
