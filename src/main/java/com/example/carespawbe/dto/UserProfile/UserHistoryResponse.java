package com.example.carespawbe.dto.UserProfile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class UserHistoryResponse {
    private Long historyId;
    private Long userId;
    private String avatar;
    private Long postId;
    private String postName;
    private LocalDate createDate;
}
