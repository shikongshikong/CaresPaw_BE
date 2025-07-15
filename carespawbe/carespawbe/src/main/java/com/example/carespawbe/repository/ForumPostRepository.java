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

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPost p " +
            "LEFT JOIN ForumPostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findByTitleKey(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPost p " +
            "LEFT JOIN ForumPostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPost> findTop2ByViews(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPost p " +
            "LEFT JOIN ForumPostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findAllShortByCreateAt(@Param("userId") Long userId);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPost p " +
//            "LEFT JOIN ForumPostSave s " +
//            "ON s.post.id = p.id and s.user.id = :userId " +
//            "WHERE p.type = :type " +
//            "ORDER BY p.createAt DESC")
//    List<ShortForumPost> findAllShortByType(@Param("type") String type);

    Optional<ForumPost> findForumPostById(Long id);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPost p " +
//            "LEFT JOIN ForumPostSave s " +
//            "ON s.post.id = p.id and s.user.id = :userId " +
//            "WHERE p.user.id = :userId " +
//            "ORDER BY p.createAt DESC")
//    List<ShortForumPost> findAllShortByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ForumPost p " +
            "SET p.viewedAmount = p.viewedAmount + 1 " +
            "WHERE p.id = :postId")
    void updateViewCount(@Param("postId") Long postId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPost p " +
            "LEFT JOIN ForumPostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "WHERE p.type = :typeId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPost> findForumPostByType(@Param("typeId") String typeId, @Param("userId") Long userId);
}
