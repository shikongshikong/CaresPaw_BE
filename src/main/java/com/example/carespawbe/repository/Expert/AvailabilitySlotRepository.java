package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlotEntity, Long> {
    List<AvailabilitySlotEntity> findByExpert_IdAndDateBetween(Long expertId, LocalDate from, LocalDate to);

    boolean existsByExpert_IdAndDateAndStartTimeAndEndTime(Long expertId, LocalDate date,
                                                           LocalTime startTime, LocalTime endTime);

    List<AvailabilitySlotEntity> findByExpert_IdAndDateAndBookedNotOrderByStartTimeAsc(
            Long expertId, LocalDate date, Integer bookedNot
    );
}
