package com.example.carespawbe.dto.Save;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SaveStatusUpdateRequest {
    private Long postId;
    private Boolean isSaved;
}
