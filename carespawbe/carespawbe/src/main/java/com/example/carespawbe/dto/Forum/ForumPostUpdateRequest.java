package com.example.carespawbe.dto.Forum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForumPostUpdateRequest {
    private String title;
    private String content;
    private int state;
    private int typeId;
}
