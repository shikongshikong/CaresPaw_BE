package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.Post;
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
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findByTitleKey(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPost> findTop2ByViews(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPost> findAllShortByCreateAt(@Param("userId") Long userId);

//    @Query("SELECT DISTINCT new com.example.carespawbe.dto.Forum.ShortForumPost(c.post.id, c.post.title, SUBSTRING(c.post.content, 1, 100), c.post.createAt, c.post.viewedAmount, c.post.commentedAmount) " +
//            "FROM PostBelongToCategory c " +
//            "LEFT JOIN PostSave s " +
//            "ON s.post.id = c.post.id and s.user.id = :userId " +
//            "WHERE c.post.type = :type " +
//            "AND c.postCategory.id IN :categoryIdList "+
//            "ORDER BY c.post.createAt DESC")
//    List<ShortForumPost> findPostByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(c.post.id, c.post.user.fullname, c.post.title, SUBSTRING(c.post.content, 1, 400), c.post.createAt, c.post.viewedAmount, c.post.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM PostBelongToCategory c " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = c.post.id and s.user.id = :userId " +
            "WHERE c.postCategory.id IN :categoryIdList "+
            "ORDER BY c.post.createAt DESC")
    List<ShortForumPost> findPostsByCategory(@Param("userId") Long userId, @Param("categoryIdList") List<Integer> categoryIdList);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(c.post.id, c.post.user.fullname, c.post.title, SUBSTRING(c.post.content, 1, 400), c.post.createAt, c.post.viewedAmount, c.post.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM PostBelongToCategory c " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = c.post.id and s.user.id = :userId " +
            "WHERE c.post.type = :type " +
            "AND c.postCategory.id IN :categoryIdList "+
            "ORDER BY c.post.createAt DESC")
    List<ShortForumPost> findPostsByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

    Optional<Post> findForumPostById(Long id);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM Post p " +
//            "LEFT JOIN PostSave s " +
//            "ON s.post.id = p.id and s.user.id = :userId " +
//            "WHERE p.user.id = :userId " +
//            "ORDER BY p.createAt DESC")
//    List<ShortForumPost> findAllShortByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Post p " +
            "SET p.viewedAmount = p.viewedAmount + 1 " +
            "WHERE p.id = :postId")
    void updateViewCount(@Param("postId") Long postId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM Post p " +
            "LEFT JOIN PostSave s " +
            "ON s.post.id = p.id and s.user.id = :userId " +
            "WHERE p.type = :typeId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPost> findForumPostByType(@Param("typeId") String typeId, @Param("userId") Long userId);
}
