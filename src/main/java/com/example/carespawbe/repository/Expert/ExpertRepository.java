package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<ExpertEntity, Long> {

    ExpertEntity findByUser(UserEntity user);

    // Lấy expert + categories + certificates để map response
    @Query("""
        select distinct e
        from ExpertEntity e
        left join fetch e.expertToCategoryEntities etc
        left join fetch etc.expertCategory c
        left join fetch e.certificateEntities cert
        left join fetch e.user u
        where e.id = :expertId
    """)
    Optional<ExpertEntity> findByExpertIdWithCvData(@Param("expertId") Long expertId);
}
