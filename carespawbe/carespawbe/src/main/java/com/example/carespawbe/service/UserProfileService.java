package com.example.carespawbe.service;

import com.example.carespawbe.dto.Follow.FollowingResponse;
import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.UserProfile.UserInfoResponse;
import com.example.carespawbe.dto.UserProfile.UserPostResponse;
import com.example.carespawbe.dto.UserProfile.UserProfileData;
import com.example.carespawbe.dto.UserProfile.UserUpdateRequest;
import com.example.carespawbe.entity.FollowingEntity;
import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.mapper.UserMapper;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowingService followingService;

    public UserProfileData getUserProfileData(Long userId){
        UserInfoResponse userResponse = userMapper.toUserInfoResponse(userService.getUserById(userId));
        List<ForumPostEntity> forumPostEntities = forumPostService.getPostListByUserId(userId);

        List<UserPostResponse> userPostResponseList = new ArrayList<>();
        userPostResponseList = postMapper.toUserPostResponseList(forumPostEntities);

        List<FollowingResponse> followingResponseList = new ArrayList<>();
        followingResponseList = followingService.getFollowingListByFollowerId(userId);

        return UserProfileData.builder()
                .user(userResponse)
                .posts(userPostResponseList)
                .followings(followingResponseList)
                .build();
    }

//    public int updateUserPost(Long postId, ForumPostRequest forumPostRequest){
//        return forumPostService.updatePostInfo(postId, forumPostRequest);
//    }

    @Transactional
    public int deleteUserPost(Long postId){
        return forumPostService.deletePost(postId);
    }

    public void updateUserProfileData(UserUpdateRequest userRequest, Long userId){
        UserEntity userEntity = userRepository.findUserById(userId);

        userEntity.setFullname(userRequest.getFullname());
        userEntity.setGender(userRequest.getGender());
        userEntity.setPhoneNumber(userRequest.getPhoneName());
        userEntity.setAvatar(userRequest.getAvatar());
        userEntity.setBirthday(userRequest.getDateOfBirth());

        userRepository.save(userEntity);
    }
}
