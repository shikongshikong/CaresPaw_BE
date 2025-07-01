package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class ShortForumPost {
    private Long id;
    private String title;
    private String summary;
    private LocalDate createAt;
    private Long likedAmount;
    private Long CommentedAmount;
}
