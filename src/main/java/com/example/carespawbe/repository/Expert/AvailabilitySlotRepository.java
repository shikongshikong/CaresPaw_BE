package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface
AvailabilitySlotRepository extends JpaRepository<AvailabilitySlotEntity, Long> {
    List<AvailabilitySlotEntity> findByExpert_IdAndDateBetween(Long expertId, LocalDate from, LocalDate to);

    boolean existsByExpert_IdAndDateAndStartTimeAndEndTime(Long expertId, LocalDate date,
                                                           java.time.LocalTime startTime, java.time.LocalTime endTime);
}
