package com.example.carespawbe.repository;

import com.example.carespawbe.entity.ForumPostCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumPostCommentRepository extends JpaRepository<ForumPostCommentEntity, Long> {
//    List<ForumPostCommentEntity> findByPostId(Long id);
List<ForumPostCommentEntity> findByForumPostEntityId(Long forumPostEntityId);
}
