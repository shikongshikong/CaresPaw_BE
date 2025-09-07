package com.example.carespawbe.repository;

import com.example.carespawbe.entity.ForumPostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumPostLikeRepository extends JpaRepository<ForumPostLikeEntity, Long> {
    Optional<ForumPostLikeEntity> findForumPostLikeEntityByUserIdAndForumPostEntityId(Long userId, Long postId);

//    boolean existsByUserIdAndForumPostEntityId(Long userId, Long forumPostEntityId);
}
