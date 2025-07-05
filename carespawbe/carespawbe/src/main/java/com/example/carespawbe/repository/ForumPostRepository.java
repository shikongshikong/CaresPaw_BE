package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findByTitleKey(@Param("keyword") String keyword);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPost> findTop2ByViews(Pageable pageable);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findAllShortByCreateAt();

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "WHERE p.type = :type " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findAllShortByType(@Param("type") String type);

    Optional<ForumPost> findForumPostById(Long id);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "WHERE p.user.id = :userId " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findAllShortByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ForumPost p " +
            "SET p.viewedAmount = p.viewedAmount + 1 " +
            "WHERE p.id = :postId")
    void updateViewCount(@Param("postId") Long postId);
}
