package com.example.carespawbe.dto.Expert.videoCall;

public record ExpertAppointmentDetailResponse(
        Long appointmentId,
        Integer status,
        String date,
        String startTime,
        String endTime,
        Integer durationMin,
        String complaint,
        String userNote,
        ExpertTodayCaseItem.PetSnapshot pet,
        ExpertTodayCaseItem.OwnerSnapshot owner,
        ExpertTodayCaseItem.MedicalRecordSnapshot medicalRecord
) {}
