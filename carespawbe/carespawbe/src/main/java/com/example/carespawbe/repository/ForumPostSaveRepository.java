package com.example.carespawbe.repository;

import com.example.carespawbe.entity.ForumPostSaveEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumPostSaveRepository extends JpaRepository<ForumPostSaveEntity, Long> {

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostSaveEntity s " +
//            "JOIN s.forumPostEntity p " +
//            "WHERE s.userEntity.id = :userId " +
//            "ORDER BY s.savedAt DESC")
//    List<ShortForumPostResponse> findShortSavedByUserId(@Param("userId") Long userId, Pageable pageable);

    List<ForumPostSaveEntity> findForumPostSavesByUserId(Long userId);

//    @Modifying
//    @Query("UPDATE ForumPostSaveEntity s SET s.")
//    int updateUserSavePost(@Param("userId") userId, @Param("postId"));

    boolean existsByUserIdAndForumPostEntityId(Long userId, Long postId);

    void deleteByUserIdAndForumPostEntityId(Long userId, Long postId);

    ForumPostSaveEntity findByUserIdAndForumPostEntityId(Long userId, Long postId);
}
