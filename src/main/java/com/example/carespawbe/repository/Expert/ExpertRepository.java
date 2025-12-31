package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.ExpertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends JpaRepository<ExpertEntity, Long> {


}
