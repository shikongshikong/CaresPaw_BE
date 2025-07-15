package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ShortForumPost {
    private Long id;
    private String title;
    private String summary;
    private LocalDate createAt;
    private Long viewedAmount;
    private Long commentedAmount;
    private boolean isSaved;
}
