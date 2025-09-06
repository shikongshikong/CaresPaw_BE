package com.example.carespawbe.repository;

import com.example.carespawbe.entity.ForumPostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumPostLikeRepository extends JpaRepository<ForumPostLikeEntity, Long> {
    Optional<ForumPostLikeEntity> findForumPostLikeEntityByUserIdAndForumPostId(Long userId, Long postId);

    boolean existsByUserIdAndForumPostId(Long userId, Long postId);
}
