package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumPostResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDate createAt;
    private Long viewedAmount;
    private Long commentedAmount;
    private Long userId;
    private String fullname;
    private String avatar;
    private boolean saved;
    private int followState;

//    lack of updateAt
}
