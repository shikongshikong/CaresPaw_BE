package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ShortForumPostResponse {
    private Long id;
    private Long userId;
    private String fullname;
    private String title;
    private String summary;
    private LocalDate createAt;
    private Long viewedAmount;
    private Long commentedAmount;
    private boolean saved;
    private int followState;
}

