package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<ExpertEntity, Long>, JpaSpecificationExecutor<ExpertEntity> {

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

    // Lấy DISTINCT expert mà user đã có appointment (không tính canceled nếu status=3)
//    @Query("""
//        select e
//        from AppointmentEntity a
//        join a.slot s
//        join s.expert e
//        where a.user.id = :userId
//          and a.status <> 3
//        group by e.id
//        order by max(s.date) desc, max(s.startTime) desc
//    """)
//    List<ExpertEntity> findDistinctExpertsBookedByUserId(@Param("userId") Long userId);
    @Query("""
    select e
    from ExpertEntity e
    where e.id in (
        select s.expert.id
        from AppointmentEntity a
        join a.slot s
        where a.user.id = :userId and a.status <> 3
    )
    order by (
        select max(s2.date) 
        from AppointmentEntity a2 
        join a2.slot s2 
        where s2.expert.id = e.id 
          and a2.user.id = :userId 
          and a2.status <> 3
    ) desc
""")
    List<ExpertEntity> findDistinctExpertsBookedByUserId(@Param("userId") Long userId);

    @Query("""
        select e from ExpertEntity e
        left join fetch e.certificateEntities c
        where e.id = :expertId
    """)
    Optional<ExpertEntity> findByIdWithCertificates(@Param("expertId") Long expertId);

    Optional<ExpertEntity> findByUser_Id(Long userId);
}
