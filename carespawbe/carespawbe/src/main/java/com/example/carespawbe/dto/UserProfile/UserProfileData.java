package com.example.carespawbe.dto.UserProfile;

import com.example.carespawbe.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class UserProfileData {
    private UserInfoResponse user;
    private List<UserPostResponse> posts;
}
