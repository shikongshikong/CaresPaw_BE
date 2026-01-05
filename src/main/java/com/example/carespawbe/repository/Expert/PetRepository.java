package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findByUserId(Long userId);

    @Query("""
        select p from PetEntity p
        left join fetch p.species s
        where p.user.id = :userId
        order by p.id desc
    """)
    List<PetEntity> findMyPets(Long userId);
}
