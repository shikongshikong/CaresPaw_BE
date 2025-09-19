package com.example.carespawbe.service;

import com.example.carespawbe.dto.Follow.FollowingResponse;
import com.example.carespawbe.dto.Forum.ShortForumPostResponse;
import com.example.carespawbe.dto.UserProfile.*;
import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.mapper.UserMapper;
import com.example.carespawbe.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowingService followingService;

    @Autowired
    private ForumPostSaveService forumPostSaveService;

    @Autowired
    private ForumPostCategoryService forumPostCategoryService;

    @Autowired
    private ForumPostHistoryService forumPostHistoryService;

    public UserProfileData getUserProfileData(Long userId){
    //  User
        UserInfoResponse userResponse = userMapper.toUserInfoResponse(userService.getUserById(userId));
    // Post
        List<ForumPostEntity> forumPostEntities = forumPostService.getPostListByUserId(userId);

        List<UserPostResponse> userPostResponseList = new ArrayList<>();
        userPostResponseList = postMapper.toUserPostResponseList(forumPostEntities);

        if (userPostResponseList != null) {
            for (UserPostResponse userPostResponse : userPostResponseList) {
                List<Integer> categoryIds = forumPostCategoryService.getCategoryListByForumPostId(userPostResponse.getId());
                userPostResponse.setCategoryList(categoryIds);
            }
        }
    // Follow
        List<FollowingResponse> followingResponseList = new ArrayList<>();
        followingResponseList = followingService.getFollowingListByFollowerId(userId);
    // Save
        List<ShortForumPostResponse> userSaveResponseList = new ArrayList<>();
        userSaveResponseList = forumPostSaveService.getSavedByUserId(userId);
    // History
        List<UserHistoryResponse> userHistoryResponseList = new ArrayList<>();
        userHistoryResponseList = forumPostHistoryService.getUserHistoryByUserId(userId);

        return UserProfileData.builder()
                .user(userResponse)
                .posts(userPostResponseList)
                .saves(userSaveResponseList)
                .followings(followingResponseList)
                .histories(userHistoryResponseList)
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
        userEntity.setPhoneNumber(userRequest.getPhoneNum());
//        userEntity.setAvatar(userRequest.getAvatar());
        userEntity.setBirthday(userRequest.getBirthday());

        userRepository.save(userEntity);
    }
}
