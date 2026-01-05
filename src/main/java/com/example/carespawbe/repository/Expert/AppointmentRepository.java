package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.AppointmentEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {

    @Query("SELECT a FROM AppointmentEntity a " +
            "WHERE a.expert.id = :expertId " +
            "AND a.slot.date = :today ORDER BY a.slot.startTime ASC")
    List<AppointmentEntity> findToday(
            @Param("expertId") Long expertId,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime
    );

//    Optional<AppointmentEntity> findFirstByExpert_IdAndSlot_StartTimeAfterOrderBySlot_StartTimeAsc(Long expertId, LocalDateTime now);

//    long countByExpert_IdAndStatusAndSlot_StartTimeBetween(
//            Long expertId,
//            int status,
//            LocalDateTime start,
//            LocalDateTime end
//    );

//    long countByExpert_IdAndSlot_StartTimeBetween(Long expertId, LocalDateTime start, LocalDateTime end);

//    @Query("SELECT COUNT(DISTINCT a.pet.id) FROM AppointmentEntity a " +
//            "WHERE a.expert.id = :expertId " +
//            "AND a.slot.startTime BETWEEN :start AND :end")
//    long countDistinctPetsByExpertAndPeriod(
//            @Param("expertId") Long expertId,
//            @Param("start") LocalDateTime start,
//            @Param("end") LocalDateTime end
//    );

    Optional<AppointmentEntity> findById(Long id);

    @Query("""
        SELECT a
        FROM AppointmentEntity a
        WHERE
            a.expert.id = :expertId AND
            (:status IS NULL OR a.status = :status)
            AND (
                :q IS NULL OR :q = '' OR
                LOWER(a.pet.name) LIKE LOWER(CONCAT('%', :q, '%')) OR
                LOWER(a.pet.user.fullname) LIKE LOWER(CONCAT('%', :q, '%'))
            )
        """)
    Page<AppointmentEntity> findWithFilters(
            @Param("expertId") Long expertId,
            @Param("status") Integer status,
            @Param("q") String q,
            Pageable pageable
    );

//    @Query(value = """
//        select a
//        from AppointmentEntity a
//        join fetch a.slot s
//        left join fetch a.user u
//        where a.expert.id = :expertId
//          and s.date between :from and :to
//    """, nativeQuery = true)
//    List<AppointmentEntity> findMonthAppointments(Long expertId, LocalDate from, LocalDate to);
    @Query(value = """
        SELECT a.*
            FROM appointment a
            INNER JOIN availability_slot s ON a.slot_id = s.slot_id
            LEFT JOIN users u ON a.user_id = u.user_id
            WHERE a.expert_id = :expertId
              AND s.date BETWEEN :from AND :to
    """, nativeQuery = true)
    List<AppointmentEntity> findMonthAppointments(Long expertId, LocalDate from, LocalDate to);

    boolean existsBySlot_Id(Long slotId);

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch a.pet p
        join fetch a.user u
        join fetch a.expert e
        where a.id = :id
    """)
    Optional<AppointmentEntity> findDetailById(@Param("id") Long id);

    @Query("""
        select a from AppointmentEntity a
        join a.slot s
        where a.user.id = :userId
        order by s.date desc, s.startTime desc
    """)
    Page<AppointmentEntity> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
        select a from AppointmentEntity a
        join a.slot s
        where a.expert.id = :expertId
        order by s.date desc, s.startTime desc
    """)
    Page<AppointmentEntity> findByExpertId(@Param("expertId") Long expertId, Pageable pageable);

    @Query("""
        select a from AppointmentEntity a
        where a.id = :id and a.user.id = :userId
    """)
    Optional<AppointmentEntity> findOwnedByUser(@Param("id") Long id, @Param("userId") Long userId);

    @Query("""
        select a from AppointmentEntity a
        where a.id = :id and a.expert.id = :expertId
    """)
    Optional<AppointmentEntity> findOwnedByExpert(@Param("id") Long id, @Param("expertId") Long expertId);

//    @Query("""
//        select a from AppointmentEntity a
//        join fetch a.slot s
//        join fetch a.pet p
//        join fetch a.user u
//        where s.expert.id = :expertId
//          and s.date = :today
//          and s.startTime > :nowTime
//          and a.status <> 3
//        order by s.startTime asc
//    """)
//    List<AppointmentEntity> findTodayRemaining(
//            @Param("expertId") Long expertId,
//            @Param("today") LocalDate today,
//            @Param("nowTime") LocalTime nowTime
//    );

    @Query(value = """
    SELECT a.* FROM appointment a
     INNER JOIN availability_slot s ON a.slot_id = s.slot_id
     INNER JOIN pet p ON a.pet_id = p.pet_id
     INNER JOIN users u ON a.user_id = u.user_id
     WHERE s.expert_id = :expertId
       AND a.status <> 3
       AND (
           s.date > :today
           OR (s.date = :today AND s.start_time > CAST(:nowTime AS TIME))
       )
     ORDER BY s.date ASC, s.start_time ASC
""", nativeQuery = true)
    List<AppointmentEntity> findUpcomingOrdered(
            @Param("expertId") Long expertId,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime
    );
//    @Query("""
//        select a from AppointmentEntity a
//        join fetch a.slot s
//        join fetch a.pet p
//        join fetch a.user u
//        where s.expert.id = :expertId
//          and a.status <> 3
//          and (
//               s.date > :today
//               or (s.date = :today and s.startTime between :nowTime and :endOfDay)
//          )
//        order by s.date asc, s.startTime asc
//    """)
//    List<AppointmentEntity> findUpcomingOrdered(
//            @Param("expertId") Long expertId,
//            @Param("today") LocalDate today,
//            @Param("nowTime") java.sql.Time nowTime,
//            @Param("endOfDay") java.sql.Time endOfDay
//    );


//    @Query("""
//        select count(a) from AppointmentEntity a
//        join a.slot s
//        where s.expert.id = :expertId
//          and a.status <> 3
//          and s.date between :startDate and :endDate
//    """)
//    long countAppointmentsInDateRange(
//            @Param("expertId") Long expertId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );
    @Query(value = """
            SELECT COUNT(a.app_id)
                FROM appointment a
                JOIN availability_slot s ON a.slot_id = s.slot_id
                WHERE s.expert_id = :expertId
                  AND a.status <> 3
                  AND s.date BETWEEN :startDate AND :endDate
        """, nativeQuery = true)
    long countAppointmentsInDateRange(
            @Param("expertId") Long expertId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

//    @Query("""
//        select count(distinct p.id) from AppointmentEntity a
//        join a.slot s
//        join a.pet p
//        where s.expert.id = :expertId
//          and a.status <> 3
//          and s.date between :startDate and :endDate
//    """)
//    long countDistinctPetsInDateRange(
//            @Param("expertId") Long expertId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );

    @Query(value = """
    SELECT COUNT(DISTINCT p.pet_id)
        FROM appointment a
        JOIN availability_slot s ON a.slot_id = s.slot_id
        JOIN pet p ON a.pet_id = p.pet_id
        WHERE s.expert_id = :expertId
          AND a.status <> 3
          AND s.date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    long countDistinctPetsInDateRange(
            @Param("expertId") Long expertId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

//    @Query("""
//        select count(a) from AppointmentEntity a
//        join a.slot s
//        where s.expert.id = :expertId
//          and s.date = :today
//          and s.startTime > :nowTime
//          and a.status <> 3
//    """)
//    long countRemainingToday(
//            @Param("expertId") Long expertId,
//            @Param("today") LocalDate today,
//            @Param("nowTime") LocalTime nowTime
//    );
    @Query(value = """
        SELECT COUNT(a.app_id)
        FROM appointment a
        JOIN availability_slot s ON a.slot_id = s.slot_id
        WHERE s.expert_id = :expertId
          AND s.date = :today
          AND s.start_time > CAST(:nowTime AS TIME)
          AND a.status <> 3
    """, nativeQuery = true)
    long countRemainingToday(
            @Param("expertId") Long expertId,
            @Param("today") LocalDate today,
            @Param("nowTime") LocalTime nowTime
    );

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch a.pet p
        join fetch a.expert e
        where a.user.id = :userId
          and a.status <> 3
          and (
            s.date > :today
            or (s.date = :today and s.startTime >= :nowTime)
          )
        order by s.date asc, s.startTime asc
    """)
    List<AppointmentEntity> findUpcomingForUser(Long userId, LocalDate today, LocalTime nowTime);

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch a.pet p
        join fetch a.expert e
        where a.user.id = :userId
          and a.status <> 3
          and (
            s.date < :today
            or (s.date = :today and s.endTime < :nowTime)
          )
        order by s.date desc, s.startTime desc
    """)
    List<AppointmentEntity> findPastForUser(Long userId, LocalDate today, LocalTime nowTime);

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch a.pet p
        join fetch a.expert e
        where a.user.id = :userId
          and a.status = 3
        order by s.date desc, s.startTime desc
    """)
    List<AppointmentEntity> findCancelledForUser(Long userId);

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch a.pet p
        join fetch a.expert e
        where a.id = :appointmentId
          and a.user.id = :userId
    """)
    Optional<AppointmentEntity> findDetailForUser(Long appointmentId, Long userId);

    @Query("""
        select a from AppointmentEntity a
        join fetch a.slot s
        join fetch s.expert e
        where a.id = :id
    """)
    Optional<AppointmentEntity> findByIdWithSlotExpert(@Param("id") Long id);

}
