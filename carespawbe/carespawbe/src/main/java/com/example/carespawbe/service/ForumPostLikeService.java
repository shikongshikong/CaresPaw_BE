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
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void updateLikeStatuses(Long userId, Long postId, int statusId) {
        Optional<ForumPostLikeEntity> isLikeReacted = forumPostLikeRepository.findForumPostLikeEntityByUserIdAndForumPostEntityId(userId, postId);

        if (isLikeReacted.isEmpty()) {
            UserEntity userEntity = userRepository.findUserById(userId);
            ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).get();
            ForumPostLikeEntity forumPostLikeEntity = ForumPostLikeEntity.builder()
                    .user(userEntity)
                    .forumPostEntity(forumPostEntity)
                    .status(statusId)
                    .build();
            forumPostLikeRepository.save(forumPostLikeEntity);
        } else {
            int currentStatus = isLikeReacted.get().getStatus();
            if (statusId == 0) {
                System.out.println("It's O");
                forumPostLikeRepository.deleteForumPostLikeEntityByForumPostEntityIdAndUserId(postId, userId);
            }
            else if (!Objects.equals(currentStatus, statusId)) {
                ForumPostLikeEntity forumPostLikeEntity = isLikeReacted.get();
                forumPostLikeEntity.setStatus(statusId);
                forumPostLikeRepository.save(forumPostLikeEntity);
            }
        }
    }

    public int getLikeStatusByPostIdAndUserId(Long postId, Long userId) {
        Optional< ForumPostLikeEntity> forumPostLikeEntity = forumPostLikeRepository.findForumPostLikeEntityByUserIdAndForumPostEntityId(userId, postId);

        if (forumPostLikeEntity.isEmpty()) return 0;
        return forumPostLikeEntity.get().getStatus();
    }
}
