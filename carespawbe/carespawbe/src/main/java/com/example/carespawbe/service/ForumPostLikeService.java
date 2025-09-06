package com.example.carespawbe.service;

import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.entity.ForumPostLikeEntity;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.repository.ForumPostLikeRepository;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ForumPostLikeService {

    @Autowired
    private ForumPostLikeRepository forumPostLikeRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    public void updateLikeStatuses(Long userId, Long postId, Long statusId) {
        Optional<ForumPostLikeEntity> isLikeReacted = forumPostLikeRepository.findForumPostLikeEntityByUserIdAndForumPostId(userId, postId);

        if (isLikeReacted.isEmpty()) {
            UserEntity userEntity = userRepository.findUserById(userId);
            ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).get();
            ForumPostLikeEntity forumPostLikeEntity = ForumPostLikeEntity.builder()
                    .user(userEntity)
                    .forumPost(forumPostEntity)
                    .status(statusId)
                    .build();
            forumPostLikeRepository.save(forumPostLikeEntity);
        } else {
            Long currentStatus = isLikeReacted.get().getStatus();
            if (!Objects.equals(currentStatus, statusId)) {
                ForumPostLikeEntity forumPostLikeEntity = isLikeReacted.get();
                forumPostLikeEntity.setStatus(statusId);
                forumPostLikeRepository.save(forumPostLikeEntity);
            }
        }
    }
}
