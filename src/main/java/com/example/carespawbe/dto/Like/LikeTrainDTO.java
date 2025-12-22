package com.example.carespawbe.dto.Like;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeTrainDTO {
    private Long userId;
    private Long postId;
    private int state;
}
