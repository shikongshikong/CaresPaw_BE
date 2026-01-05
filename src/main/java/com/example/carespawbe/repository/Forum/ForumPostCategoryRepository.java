package com.example.carespawbe.repository.Forum;

import com.example.carespawbe.entity.Forum.ForumPostTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumPostCategoryRepository extends JpaRepository<ForumPostTypeEntity, Long> {
}
