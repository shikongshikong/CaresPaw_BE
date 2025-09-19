package com.example.carespawbe.repository;

import com.example.carespawbe.entity.FollowingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowingRepository extends JpaRepository<FollowingEntity, Long> {
    List<FollowingEntity> findAllByFollowerId(Long id);

    FollowingEntity findByFollowerIdAndFolloweeId(Long follower_id, Long following_id);

    boolean existsFollowingEntitiesByFollowerIdAndFolloweeId(Long follower_id, Long following_id);
}
