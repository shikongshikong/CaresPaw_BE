package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
            "FROM ForumPost p " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ShortForumPost> findByTitle(@Param("keyword") String keyword);

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
}
