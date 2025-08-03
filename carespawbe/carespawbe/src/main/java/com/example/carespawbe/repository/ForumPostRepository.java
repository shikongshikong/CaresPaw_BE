package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ShortForumPostResponse;
import com.example.carespawbe.entity.ForumPostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPostEntity, Long> {

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPostResponse> findByTitleKey(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPostResponse> findTop2ByViews(Pageable pageable, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPostResponse> findAllShortByCreateAt(@Param("userId") Long userId);

//    @Query("SELECT DISTINCT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 100), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount) " +
//            "FROM ForumPostToCategoryEntity c " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.userEntity.id = :userId " +
//            "WHERE c.forumPostEntity.type = :type " +
//            "AND c.forumPostCategoryEntity.id IN :categoryIdList "+
//            "ORDER BY c.forumPostEntity.createAt DESC")
//    List<ShortForumPostResponse> findPostByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.user.fullname, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 400), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostToCategoryEntity c " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.user.id = :userId " +
            "WHERE c.forumPostCategoryEntity.id IN :categoryIdList "+
            "ORDER BY c.forumPostEntity.createAt DESC")
    List<ShortForumPostResponse> findPostsByCategory(@Param("userId") Long userId, @Param("categoryIdList") List<Integer> categoryIdList);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.user.fullname, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 400), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount, " +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostToCategoryEntity c " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.user.id = :userId " +
            "WHERE c.forumPostEntity.type = :type " +
            "AND c.forumPostCategoryEntity.id IN :categoryIdList "+
            "ORDER BY c.forumPostEntity.createAt DESC")
    List<ShortForumPostResponse> findPostsByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

    Optional<ForumPostEntity> findForumPostById(Long id);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM ForumPostEntity p " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = p.id and s.userEntity.id = :userId " +
//            "WHERE p.userEntity.id = :userId " +
//            "ORDER BY p.createAt DESC")
//    List<ShortForumPostResponse> findAllShortByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ForumPostEntity p " +
            "SET p.viewedAmount = p.viewedAmount + 1 " +
            "WHERE p.id = :postId")
    void updateViewCount(@Param("postId") Long postId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "WHERE p.type = :typeId " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPostResponse> findForumPostByType(@Param("typeId") String typeId, @Param("userId") Long userId);

//    userEntity profile
    List<ForumPostEntity> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ForumPostEntity p " +
            "SET p.title = :title," +
            "p.content = :content," +
            "p.updateAt = :updateAt," +
            "p.state = :state " +
            "WHERE p.id = :userId"
    )
    int updatePost(@Param("postId") Long postId,
                   @Param("title") String title,
                   @Param("content") String content,
                   @Param("updateAt") LocalDate updateAt,
                   @Param("status") String state);

    int removePostById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE ForumPostEntity p " +
            "SET p.commentedAmount = p.commentedAmount + 1 " +
            "WHERE p.id = :postId")
    void updateCmCount(@Param("postId") Long postId);
}
