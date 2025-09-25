package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Forum.ForumPostCommentRequest;
import com.example.carespawbe.dto.Forum.ForumPostCommentResponse;
import com.example.carespawbe.entity.Forum.ForumPostCommentEntity;
import com.example.carespawbe.mapper.Forum.ForumPostCommentMapper;
import com.example.carespawbe.repository.Forum.FollowingRepository;
import com.example.carespawbe.repository.Forum.ForumPostCommentRepository;
import com.example.carespawbe.repository.Auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumPostCommentService {

    @Autowired
    private ForumPostCommentRepository forumPostCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumPostCommentMapper forumPostCommentMapper;

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private FollowingRepository  followingRepository;

    public ForumPostCommentResponse addPostComment(ForumPostCommentRequest forumPostCommentRequest, Long userId) {
//        ForumPostEntity forumPostEntity = forumPostRepository.findById(forumPostCommentRequest.getPostId()).orElse(null);
//        UserEntity userEntity = userRepository.findById(forumPostCommentRequest.getUserId()).orElse(null);
        ForumPostCommentEntity cm = forumPostCommentMapper.toPostComment(forumPostCommentRequest);
//        cm.setForumPostEntity(forumPostEntity);
//        cm.setUserEntity(userEntity);
        forumPostCommentRepository.save(cm);
        forumPostService.increaseCommentCount(forumPostCommentRequest.getPostId());

        ForumPostCommentResponse cmRes = forumPostCommentMapper.toCommentResponse(cm);

        int followState = 2;

        boolean followed = followingRepository.existsFollowingEntitiesByFollowerIdAndFolloweeId(userId, cm.getUser().getId());

        if (userId.equals(cm.getUser().getId())) followState = 0;

        else if (followed){
            followState = 1;
        }

        else followState = 2;

        cmRes.setFollowState(followState);
        cmRes.setFullname(cm.getUser().getFullname());

        return cmRes;
    }

    public List<ForumPostCommentResponse> getPostCommentsByPostId(Long postId) {
        if (postId != null) {
            List<ForumPostCommentEntity> posts = forumPostCommentRepository.findByForumPostEntityId(postId);
            return forumPostCommentMapper.toCommentResponseList(posts);
        }
        return null;
    }

}
