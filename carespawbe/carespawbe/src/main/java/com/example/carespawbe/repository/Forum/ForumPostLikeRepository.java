package com.example.carespawbe.repository.Forum;

import com.example.carespawbe.entity.Forum.ForumPostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForumPostLikeRepository extends JpaRepository<ForumPostLikeEntity, Long> {
    Optional<ForumPostLikeEntity> findForumPostLikeEntityByUserIdAndForumPostEntityId(Long userId, Long postId);

    void deleteForumPostLikeEntityByForumPostEntityIdAndUserId(Long forumPostEntityId, Long userId);

//    boolean existsByUserIdAndForumPostEntityId(Long userId, Long forumPostEntityId);
}
