package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<PetEntity, Long> {

    List<PetEntity> findByUserId(Long userId);
}
