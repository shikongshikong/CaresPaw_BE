package com.example.carespawbe.dto.Expert.videoCall;

import lombok.Builder;

public record ExpertTodayCaseItem(
        Long appointmentId,
        String date,
        String startTime,
        String endTime,
        Integer durationMin,
        Integer status,
        String complaint,
        String userNote,
        PetSnapshot pet,
        OwnerSnapshot owner,
        MedicalRecordSnapshot medicalRecord
) {
    public record PetSnapshot(Long id, String name, String breed, Integer gender, String dateOfBirth,
                              String imageUrl, String description, String microchipId, String allergies,
                              String chronicDiseases, Double weight) {}
    public record OwnerSnapshot(Long id, String fullName, String phone) {}
    public record MedicalRecordSnapshot(String expertNote, String expertAdvise) {}
}
