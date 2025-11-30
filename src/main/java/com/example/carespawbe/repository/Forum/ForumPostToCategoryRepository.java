package com.example.carespawbe.repository.Forum;

import com.example.carespawbe.entity.Forum.ForumPostToCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumPostToCategoryRepository extends JpaRepository<ForumPostToCategoryEntity, Long> {
//    @Override
//    List<ForumPostToCategoryEntity> saveAll(Iterable<S> entities);

    List<ForumPostToCategoryEntity> findAllByForumPostEntityId(Long forumPostEntityId);
}
