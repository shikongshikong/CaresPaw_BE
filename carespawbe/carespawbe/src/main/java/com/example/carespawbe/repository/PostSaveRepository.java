package com.example.carespawbe.repository;

import com.example.carespawbe.entity.PostSave;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSaveRepository extends JpaRepository<PostSave, Long> {

//    @Query("SELECT new com.example.carespawbe.dto.Forum.ShortForumPost(p.id, p.title, SUBSTRING(p.content, 1, 100), p.createAt, p.viewedAmount, p.commentedAmount) " +
//            "FROM PostSave s " +
//            "JOIN s.post p " +
//            "WHERE s.user.id = :userId " +
//            "ORDER BY s.savedAt DESC")
//    List<ShortForumPost> findShortSavedByUserId(@Param("userId") Long userId, Pageable pageable);

    List<PostSave> findForumPostSavesByUserId(Long userId, Pageable pageable);

//    @Modifying
//    @Query("UPDATE PostSave s SET s.")
//    int updateUserSavePost(@Param("userId") userId, @Param("postId"));

    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByUserIdAndPostId(Long userId, Long postId);
}
