package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPostHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostHistoryRepository extends JpaRepository<ForumPostHistory, Long> {

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostHistory h " +
//            "JOIN h.post p " +
//            "WHERE h.user.id = :userId " +
//            "ORDER BY h.createdAt DESC")
//    List<ShortForumPost> findAllShortHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostHistory h " +
//            "JOIN h.post p " +
//            "WHERE h.user.id = :userId " +
//            "ORDER BY h.createdAt DESC")
//    List<ShortForumPost> find5ShortHistoryByUserId(@Param("userId") Long userId, Pageable pageable);

    List<ForumPostHistory> findForumPostHistoriesByUserId(Long userId, Pageable pageable);

    List<ForumPostHistory> findByUserIdAndPostIdAndCreatedAt(Long userId, Long postId, LocalDate createdAt);
}
