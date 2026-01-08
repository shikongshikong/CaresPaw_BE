package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.ExpertToExpertCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpertToExpertCategoryRepository extends JpaRepository<ExpertToExpertCategoryEntity, Long> {

    List<ExpertToExpertCategoryEntity> findByExpert_Id(Long expertId);

}
