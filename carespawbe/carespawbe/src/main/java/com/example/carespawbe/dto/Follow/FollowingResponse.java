package com.example.carespawbe.dto.Follow;

import lombok.Data;

@Data
public class FollowingResponse {
    private Long userId;
    private String fullname;
    private String avatar;
}
