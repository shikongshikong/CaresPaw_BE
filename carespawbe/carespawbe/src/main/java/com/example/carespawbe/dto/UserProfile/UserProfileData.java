package com.example.carespawbe.dto.UserProfile;

import com.example.carespawbe.dto.Follow.FollowingResponse;
import com.example.carespawbe.dto.Forum.ShortForumPostResponse;
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
    private List<ShortForumPostResponse> saves;
    private List<FollowingResponse> followings;
    private List<UserHistoryResponse> histories;
}
