package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ForumPostTrainingDTO {
    private Long postId;
    private String title;
    private String content;
    private LocalDate createAt;
    private int state;
    private int typeId;
    private List<Integer> categoryIds;
}
