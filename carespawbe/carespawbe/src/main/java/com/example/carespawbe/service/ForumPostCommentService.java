package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostCommentRequest;
import com.example.carespawbe.dto.Forum.ForumPostCommentResponse;
import com.example.carespawbe.entity.ForumPostCommentEntity;
import com.example.carespawbe.mapper.ForumPostCommentMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.repository.ForumPostCommentRepository;
import com.example.carespawbe.repository.UserRepository;
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

    public ForumPostCommentResponse addPostComment(ForumPostCommentRequest forumPostCommentRequest) {
//        ForumPostEntity forumPostEntity = forumPostRepository.findById(forumPostCommentRequest.getPostId()).orElse(null);
//        UserEntity userEntity = userRepository.findById(forumPostCommentRequest.getUserId()).orElse(null);
        ForumPostCommentEntity cm = forumPostCommentMapper.toPostComment(forumPostCommentRequest);
//        cm.setForumPostEntity(forumPostEntity);
//        cm.setUserEntity(userEntity);
        forumPostCommentRepository.save(cm);
        forumPostService.increaseCommentCount(forumPostCommentRequest.getPostId());
        return forumPostCommentMapper.toCommentResponse(cm);
    }

    public List<ForumPostCommentResponse> getPostCommentsByPostId(Long postId) {
        if (postId != null) {
            List<ForumPostCommentEntity> posts = forumPostCommentRepository.findByForumPostEntityId(postId);
            return forumPostCommentMapper.toCommentResponseList(posts);
        }
        return null;
    }

}
