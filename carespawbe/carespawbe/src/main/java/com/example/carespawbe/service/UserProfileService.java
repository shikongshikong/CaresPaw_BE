package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.UserProfile.UserInfoResponse;
import com.example.carespawbe.dto.UserProfile.UserPostResponse;
import com.example.carespawbe.dto.UserProfile.UserProfileData;
import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumPostMapper postMapper;

    public UserProfileData getUserProfileData(Long userId){
        UserInfoResponse userResponse = userMapper.toUserInfoResponse(userService.getUserById(userId));
        List<ForumPostEntity> forumPostEntities = forumPostService.getPostListByUserId(userId);

        List<UserPostResponse> userPostResponseList = new ArrayList<>();
        userPostResponseList = postMapper.toUserPostResponseList(forumPostEntities);

        UserProfileData data = UserProfileData.builder()
                .user(userResponse)
                .posts(userPostResponseList)
                .build();

        return data;
    }

    public int updateUserPost(Long postId, ForumPostRequest forumPostRequest){
        return forumPostService.updatePostInfo(postId, forumPostRequest);
    }

    @Transactional
    public int deleteUserPost(Long postId){
        return forumPostService.deletePost(postId);
    }
}
