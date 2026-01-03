package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.ExpertEarningEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExpertEarningRepository extends JpaRepository<ExpertEntity, Long>, JpaSpecificationExecutor<ExpertEarningEntity> {

    @Query("SELECT SUM(e.expertGain) FROM ExpertEarningEntity e " +
            "WHERE e.createAt BETWEEN :start AND :end")
    BigDecimal sumEarningsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
