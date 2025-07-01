package com.example.carespawbe.repository;

import com.example.carespawbe.entity.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpertRepository extends JpaRepository<Expert, Long> {
//    just take short with certificate
    List<Expert> findAllExperts();

    Expert findExpertById(Long id);
}
