package com.example.carespawbe.repository;

import com.example.carespawbe.entity.ForumPostHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ForumPostHistoryRepository extends JpaRepository<ForumPostHistoryEntity, Long> {

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostHistoryEntity h " +
//            "JOIN h.forumPostEntity p " +
//            "WHERE h.userEntity.id = :userId " +
//            "ORDER BY h.createdAt DESC")
//    List<ShortForumPostResponse> findAllShortHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostHistoryEntity h " +
//            "JOIN h.forumPostEntity p " +
//            "WHERE h.userEntity.id = :userId " +
//            "ORDER BY h.createdAt DESC")
//    List<ShortForumPostResponse> find5ShortHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

//    List<ForumPostHistoryEntity> findForumPostHistoryEntityByUserId(Long userId, Pageable pageable);
    List<ForumPostHistoryEntity> findForumPostHistoryEntitiesByUserId(Long userId, Pageable pageable);

//    List<ForumPostHistoryEntity> findByUserIdAndPostIdAndCreatedAt(Long userId, Long postId, LocalDate createdAt);
    List<ForumPostHistoryEntity> findByUserIdAndForumPostEntityIdAndCreatedAt(Long userId, Long postId, LocalDate createdAt);
}
