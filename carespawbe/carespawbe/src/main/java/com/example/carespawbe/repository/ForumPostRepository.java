package com.example.carespawbe.repository;

import com.example.carespawbe.dto.Forum.ForumPostResponse;
import com.example.carespawbe.dto.Forum.ShortForumPostResponse;
import com.example.carespawbe.entity.ForumPostEntity;
import org.springframework.data.domain.Page;
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

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END ) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) )" +
            "AND p.state <> 0 " +
            "ORDER BY p.createAt DESC")
    List<ShortForumPostResponse> findByTitleKey(@Param("keyword") String keyword, @Param("userId") Long userId);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END ) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE p.state <> 0 " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPostResponse> findTop2ByViews(Pageable pageable, @Param("userId") Long userId);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount, " +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
//            "CASE " +
//            "WHEN p.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
//            "ELSE 2 END ) " +
//            "FROM ForumPostEntity p " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
//            "ORDER BY p.createAt DESC")
//    List<ShortForumPostResponse> findAllShortByCreateAt(@Param("userId") Long userId);

//    @Query("SELECT DISTINCT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 100), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount) " +
//            "FROM ForumPostToCategoryEntity c " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.userEntity.id = :userId " +
//            "WHERE c.forumPostEntity.type = :type " +
//            "AND c.forumPostCategoryEntity.id IN :categoryIdList "+
//            "ORDER BY c.forumPostEntity.createAt DESC")
//    List<ShortForumPostResponse> findPostByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.user.id, c.forumPostEntity.user.fullname, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 240), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount, " +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
//            "CASE " +
//            "WHEN c.forumPostEntity.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = c.forumPostEntity.user.id THEN 1 " +
//            "ELSE 2 END ) " +
//            "FROM ForumPostToCategoryEntity c " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and c.forumPostEntity.user.id = f.followee.id " +
//            "WHERE c.forumPostCategoryEntity.id IN :categoryIdList "+
//            "ORDER BY c.forumPostEntity.createAt DESC")
//    List<ShortForumPostResponse> findPostsByCategory(@Param("userId") Long userId, @Param("categoryIdList") List<Integer> categoryIdList);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.user.id, c.forumPostEntity.user.fullname, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 240), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount, " +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END," +
//            "CASE " +
//            "WHEN c.forumPostEntity.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = c.forumPostEntity.user.id THEN 1 " +
//            "ELSE 2 END ) " +
//            "FROM ForumPostToCategoryEntity c " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and c.forumPostEntity.user.id = f.followee.id " +
//            "WHERE c.forumPostEntity.typeId = :type " +
//            "AND c.forumPostCategoryEntity.id IN :categoryIdList "+
//            "ORDER BY c.forumPostEntity.createAt DESC")
//    List<ShortForumPostResponse> findPostsByTypeAndCategory(@Param("userId") Long userId, @Param("type") String type, @Param("categoryIdList") List<Integer> categoryIdList);

    Optional<ForumPostEntity> findForumPostById(Long id);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ForumPostResponse(p.id, p.title, p.content, p.createAt, p.viewedAmount, p.commentedAmount, p.user.id, p.user.fullname, p.user.avatar, " +
            "CASE WHEN s IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END " +
            ") " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
//            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
//            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "WHERE p.id = :postId " +
            "ORDER BY p.createAt DESC")
    Optional<ForumPostResponse> findForumPostDetailById(@Param("userId") Long userId, @Param("postId") Long postId);

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

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 400), p.createAt, p.viewedAmount, p.commentedAmount," +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END," +
//            "CASE " +
//            "WHEN p.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
//            "ELSE 2 END ) " +
//            "FROM ForumPostEntity p " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
//            "WHERE p.type = :typeId " +
//            "ORDER BY p.viewedAmount DESC")
//    List<ShortForumPostResponse> findForumPostByType(@Param("typeId") int typeId, @Param("userId") Long userId);

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

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), " +
            "p.createAt, " +
            "p.viewedAmount, p.commentedAmount, " +
            "CASE WHEN s IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE p.state <> 0 " +
            "ORDER BY p.createAt DESC")
    Page<ShortForumPostResponse> findPageShortByCreateAt(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE p.typeId = :typeId " +
            "AND p.state <> 0 " +
            "ORDER BY p.viewedAmount DESC")
    Page<ShortForumPostResponse> findPageShortsByType(@Param("userId") Long userId, @Param("typeId") int typeId, Pageable pageable);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(c.forumPostEntity.id, c.forumPostEntity.user.id, c.forumPostEntity.user.fullname, c.forumPostEntity.title, SUBSTRING(c.forumPostEntity.content, 1, 240), c.forumPostEntity.createAt, c.forumPostEntity.viewedAmount, c.forumPostEntity.commentedAmount, " +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
//            "CASE " +
//            "WHEN c.forumPostEntity.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = c.forumPostEntity.user.id THEN 1 " +
//            "ELSE 2 END ) " +
//            "FROM ForumPostToCategoryEntity c " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = c.forumPostEntity.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and c.forumPostEntity.user.id = f.followee.id " +
//            "WHERE c.forumPostCategoryEntity.id IN :categoryIdList "+
//            "ORDER BY c.forumPostEntity.createAt DESC")
//    Page<ShortForumPostResponse> findPageShortsByCategory(@Param("userId") Long userId, @Param("categoryIdList") List<Integer> categoryIdList, Pageable pageable);

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), p.createAt, p.viewedAmount, p.commentedAmount," +
//            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
//            "CASE " +
//            "WHEN p.user.id = :userId THEN 0 " +
//            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
//            "ELSE 2 END) " +
//            "FROM ForumPostEntity p " +
//            "LEFT JOIN ForumPostSaveEntity s " +
//            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
//            "LEFT JOIN FollowingEntity f " +
//            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
//            "WHERE p.typeId = :typeId " +
////            "AND p.id IN " +
//            "AND EXISTS " +
//            "(SELECT DISTINCT p2.id " +
//            "FROM ForumPostEntity p2 " +
//            "LEFT JOIN ForumPostToCategoryEntity c " +
//            "ON p2.id = c.forumPostEntity.id " +
////            "WHERE c.forumPostEntity.id = p2.id " +
//            "WHERE c.forumPostCategoryEntity.id IN (:categoryList)) " +
//            "ORDER BY p.viewedAmount DESC")
//    Page<ShortForumPostResponse> findPageShortByTypeAndToCategories(@Param("userId") Long userId, @Param("typeId") int typeId, @Param("categoryList") List<Integer> categoryList, Pageable pageable);

    @Query("""
        SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(
            p.id, p.user.id, p.user.fullname, p.title,
            SUBSTRING(p.content, 1, 240),
            p.createAt, p.viewedAmount, p.commentedAmount,
            CASE WHEN s.id IS NOT NULL THEN true ELSE false END,
            CASE
                WHEN p.user.id = :userId THEN 0
                WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1
                ELSE 2
            END
        )
        FROM ForumPostEntity p
        LEFT JOIN ForumPostSaveEntity s
            ON s.forumPostEntity.id = p.id AND s.user.id = :userId
        LEFT JOIN FollowingEntity f
            ON f.follower.id = :userId AND p.user.id = f.followee.id
        WHERE p.typeId = :typeId
          AND p.state <> 0
          AND EXISTS (
              SELECT 1
              FROM ForumPostToCategoryEntity c
              WHERE c.forumPostEntity.id = p.id
                AND c.forumPostCategoryEntity.id IN (:categoryList)
          )
        ORDER BY p.viewedAmount DESC
    """)
    Page<ShortForumPostResponse> findPageShortByTypeAndToCategories(
            @Param("userId") Long userId,
            @Param("typeId") int typeId,
            @Param("categoryList") List<Integer> categoryList,
            Pageable pageable
    );

    @Query("""
        SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(
            p.id, p.user.id, p.user.fullname, p.title,
            SUBSTRING(p.content, 1, 240),
            p.createAt, p.viewedAmount, p.commentedAmount,
            CASE WHEN s.id IS NOT NULL THEN true ELSE false END,
            CASE
                WHEN p.user.id = :userId THEN 0
                WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1
                ELSE 2
            END
        )
        FROM ForumPostEntity p
        LEFT JOIN ForumPostSaveEntity s
            ON s.forumPostEntity.id = p.id AND s.user.id = :userId
        LEFT JOIN FollowingEntity f
            ON f.follower.id = :userId AND p.user.id = f.followee.id
        WHERE p.state <> 0
        AND EXISTS (
              SELECT 1
              FROM ForumPostToCategoryEntity c
              WHERE c.forumPostEntity.id = p.id
                AND c.forumPostCategoryEntity.id IN (:categoryList)
          )
        ORDER BY p.viewedAmount DESC
    """)
    Page<ShortForumPostResponse> findPageShortsByCategory(@Param("userId") Long userId, @Param("categoryList") List<Integer> categoryList, Pageable pageable);

    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPostResponse(p.id, p.user.id, p.user.fullname, p.title, SUBSTRING(p.content, 1, 240), p.createAt, p.viewedAmount, p.commentedAmount," +
            "CASE WHEN s.id IS NOT NULL THEN true ELSE false END, " +
            "CASE " +
            "WHEN p.user.id = :userId THEN 0 " +
            "WHEN f.follower.id = :userId AND f.followee.id = p.user.id THEN 1 " +
            "ELSE 2 END) " +
            "FROM ForumPostEntity p " +
            "LEFT JOIN ForumPostSaveEntity s " +
            "ON s.forumPostEntity.id = p.id and s.user.id = :userId " +
            "LEFT JOIN FollowingEntity f " +
            "ON f.follower.id = :userId and p.user.id = f.followee.id " +
            "WHERE s.id IS NOT NULL " +
            "AND p.state <> 0 " +
            "ORDER BY p.viewedAmount DESC")
    List<ShortForumPostResponse> findSaveShortsByUserId(@Param("userId") Long userId);
}
