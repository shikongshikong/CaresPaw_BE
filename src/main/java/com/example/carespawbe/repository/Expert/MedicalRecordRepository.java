package com.example.carespawbe.repository.Expert;

import com.example.carespawbe.entity.Expert.MedicalRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
    Optional<MedicalRecordEntity> findByAppointment_Id(Long appointmentId);

    List<MedicalRecordEntity> findByAppointment_IdIn(Collection<Long> appointmentIds);
}

