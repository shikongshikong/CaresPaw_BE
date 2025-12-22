package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Like.LikeTrainDTO;
import com.example.carespawbe.entity.Forum.ForumPostEntity;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import com.example.carespawbe.entity.Forum.ForumPostLikeEntity;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Forum.ForumPostLikeRepository;
import com.example.carespawbe.repository.Forum.ForumPostRepository;
import com.example.carespawbe.repository.Auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    public List<LikeTrainDTO> getAllLike() {
        List<ForumPostLikeEntity> likeList = forumPostLikeRepository.findAll();
        List<LikeTrainDTO> likeTrainDTOS = likeList.stream()
                .map(l -> new LikeTrainDTO(
                        l.getUser().getId(),
                        l.getId(),
                        l.getStatus()
                )).toList();
        return likeTrainDTOS;
    }
}
