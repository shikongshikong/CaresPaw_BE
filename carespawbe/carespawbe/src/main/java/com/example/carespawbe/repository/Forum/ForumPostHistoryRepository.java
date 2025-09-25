package com.example.carespawbe.repository.Forum;

import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
import com.example.carespawbe.dto.UserProfile.UserHistoryResponse;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("SELECT new com.example.carespawbe.dto.History.ForumPostHistoryTagResponse(p.id, p.user.id, p.user.fullname, p.title, p.createAt, p.viewedAmount, p.commentedAmount, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END ) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostHistoryEntity h " +
            "ON h.forumPostEntity.id = p.id and h.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE h.user.id = :userId " +
            "ORDER BY h.createdAt DESC")
    List<ForumPostHistoryTagResponse> findForumPostHistoryEntityByUserIdHasFollow(@Param("userId") Long userId, Pageable pageable);

//    List<ForumPostHistoryEntity> findForumPostHistoryEntitiesByUserId(Long userId, Pageable pageable);

//    List<ForumPostHistoryEntity> findByUserIdAndPostIdAndCreatedAt(Long userId, Long postId, LocalDate createdAt);
    List<ForumPostHistoryEntity> findByUserIdAndForumPostEntityIdAndCreatedAt(Long userId, Long postId, LocalDate createdAt);

    @Query("SELECT new com.example.carespawbe.dto.UserProfile.UserHistoryResponse(h.id, p.user.id, p.user.avatar, p.id, p.title, h.createdAt) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostHistoryEntity h " +
            "ON h.forumPostEntity.id = p.id and h.user.id = :userId " +
            "WHERE h.user.id = :userId " +
            "ORDER BY h.createdAt DESC")
    List<UserHistoryResponse> findHistoryEntityByUserId(@Param("userId") Long userId);
}
