package com.example.carespawbe.service.Expert.videoCalling;

import com.example.carespawbe.dto.Expert.videoCall.*;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.MedicalRecordEntity;
import com.example.carespawbe.entity.Expert.PetEntity;
import com.example.carespawbe.enums.CallJoinState;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.repository.Expert.MedicalRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertCallingService {

    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    private final String jitsiDomain = "https://meet.jit.si";

    private void validateSlot(AvailabilitySlotEntity s) {
        if (s == null || s.getDate() == null || s.getStartTime() == null || s.getEndTime() == null) {
            throw new IllegalStateException("Slot time invalid");
        }
    }

    private int durationMinFromSlot(AvailabilitySlotEntity s) {
        long minutes = Duration.between(s.getStartTime(), s.getEndTime()).toMinutes();
        return (int) Math.max(0, minutes);
    }

    private String hhmm(LocalTime t) {
        return t == null ? "" : t.toString().substring(0, 5);
    }

    public List<ExpertTodayCaseItem> getTodayCases(Long expertId) {
        LocalDate today = LocalDate.now();
        List<AppointmentEntity> list = appointmentRepository.findExpertToday(expertId, today);

        // tránh N+1: load medical records theo batch
        List<Long> appIds = list.stream().map(AppointmentEntity::getId).toList();
        Map<Long, MedicalRecordEntity> mrMap = medicalRecordRepository.findByAppointment_IdIn(appIds)
                .stream().collect(Collectors.toMap(mr -> mr.getAppointment().getId(), mr -> mr));

        return list.stream().map(a -> toTodayItem(a, mrMap.get(a.getId()))).toList();
    }

    public ExpertAppointmentDetailResponse getDetail(Long expertId, Long appointmentId) {
        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        MedicalRecordEntity mr = medicalRecordRepository.findByAppointment_Id(appointmentId).orElse(null);

        AvailabilitySlotEntity s = a.getSlot();
        validateSlot(s);

        return new ExpertAppointmentDetailResponse(
                a.getId(),
                a.getStatus(),
                s.getDate().toString(),
                hhmm(s.getStartTime()),
                hhmm(s.getEndTime()),
                durationMinFromSlot(s),
                a.getUserNote(), // complaint bạn chưa có field riêng -> dùng userNote cho UI reason
                a.getUserNote(),
                toPet(a.getPet()),
                toOwner(a.getUser()),
                toMedicalRecordSnapshot(mr)
        );
    }

    public JoinCallResponse joinCall(Long expertId, Long appointmentId) {
        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        if (a.getStatus() == 3) return endedJoin(a);

        AvailabilitySlotEntity s = a.getSlot();
        validateSlot(s);

        LocalDate date = s.getDate();
        LocalTime start = s.getStartTime();
        LocalTime end = s.getEndTime();
        int durationMin = durationMinFromSlot(s);

        ZoneId zone = ZoneId.systemDefault();
        Instant now = Instant.now();
        Instant startInstant = ZonedDateTime.of(date, start, zone).toInstant();
        Instant endInstant = ZonedDateTime.of(date, end, zone).toInstant();

        long nowMs = now.toEpochMilli();
        long startMs = startInstant.toEpochMilli();
        long endMs = endInstant.toEpochMilli();

        if (nowMs < startMs) {
            int secondsToStart = (int) Math.max(0, (startMs - nowMs) / 1000);
            return new JoinCallResponse(
                    CallJoinState.WAITING, a.getId(),
                    date.toString(), hhmm(start), hhmm(end), durationMin,
                    nowMs, startMs, endMs,
                    secondsToStart, null, null
            );
        }

        if (nowMs > endMs) {
            return new JoinCallResponse(
                    CallJoinState.ENDED, a.getId(),
                    date.toString(), hhmm(start), hhmm(end), durationMin,
                    nowMs, startMs, endMs,
                    null, 0, null
            );
        }

        int remainingSec = (int) Math.max(0, (endMs - nowMs) / 1000);

        // ✅ match user side
        String roomName = "appt_" + a.getId();
        String joinUrl = jitsiDomain + "/" + roomName;

        return new JoinCallResponse(
                CallJoinState.ACTIVE, a.getId(),
                date.toString(), hhmm(start), hhmm(end), durationMin,
                nowMs, startMs, endMs,
                null, remainingSec,
                new JitsiCallInfo(roomName, joinUrl, null)
        );
    }

    @Transactional
    public ExpertTodayCaseItem.MedicalRecordSnapshot saveMedicalRecord(Long expertId, Long appointmentId, SaveMedicalRecordRequest req) {
        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));

        // verify petId nếu FE gửi
        if (req.petId() != null && a.getPet() != null && !a.getPet().getId().equals(req.petId())) {
            throw new IllegalArgumentException("Pet mismatch");
        }

        MedicalRecordEntity mr = medicalRecordRepository.findByAppointment_Id(appointmentId)
                .orElseGet(() -> {
                    MedicalRecordEntity x = new MedicalRecordEntity();
                    x.setAppointment(a);
                    x.setPet(a.getPet());
                    x.setExpert(a.getExpert());
                    return x;
                });

        mr.setExpertNote(req.expertNote());
        mr.setExpertAdvise(req.expertAdvise());
        medicalRecordRepository.save(mr);

        return new ExpertTodayCaseItem.MedicalRecordSnapshot(
                mr.getExpertNote() == null ? "" : mr.getExpertNote(),
                mr.getExpertAdvise() == null ? "" : mr.getExpertAdvise()
        );
    }

    // ===== mapping DTO =====

    private JoinCallResponse endedJoin(AppointmentEntity a) {
        AvailabilitySlotEntity s = a.getSlot();
        validateSlot(s);

        ZoneId zone = ZoneId.systemDefault();
        Instant now = Instant.now();
        Instant start = ZonedDateTime.of(s.getDate(), s.getStartTime(), zone).toInstant();
        Instant end = ZonedDateTime.of(s.getDate(), s.getEndTime(), zone).toInstant();

        return new JoinCallResponse(
                CallJoinState.ENDED, a.getId(),
                s.getDate().toString(), hhmm(s.getStartTime()), hhmm(s.getEndTime()),
                durationMinFromSlot(s),
                now.toEpochMilli(), start.toEpochMilli(), end.toEpochMilli(),
                null, 0, null
        );
    }

    private ExpertTodayCaseItem toTodayItem(AppointmentEntity a, MedicalRecordEntity mr) {
        AvailabilitySlotEntity s = a.getSlot();
        validateSlot(s);

        return new ExpertTodayCaseItem(
                a.getId(),
                s.getDate().toString(),
                hhmm(s.getStartTime()),
                hhmm(s.getEndTime()),
                durationMinFromSlot(s),
                a.getStatus(),
                a.getUserNote(), // complaint thay thế tạm
                a.getUserNote(),
                toPet(a.getPet()),
                toOwner(a.getUser()),
                toMedicalRecordSnapshot(mr)
        );
    }

    private ExpertTodayCaseItem.PetSnapshot toPet(PetEntity p) {
        if (p == null) return null;
        return new ExpertTodayCaseItem.PetSnapshot(
                p.getId(), p.getName(), p.getBreed(), p.getGender(),
                p.getDateOfBirth() == null ? null : p.getDateOfBirth().toString(),
                p.getImageUrl(), p.getDescription(), p.getMicrochipId(),
                p.getAllergies(), p.getChronic_diseases(), p.getWeight()
        );
    }

    private ExpertTodayCaseItem.OwnerSnapshot toOwner(UserEntity u) {
        if (u == null) return null;
        return new ExpertTodayCaseItem.OwnerSnapshot(u.getId(), u.getFullname(), u.getPhoneNumber());
    }

    private ExpertTodayCaseItem.MedicalRecordSnapshot toMedicalRecordSnapshot(MedicalRecordEntity mr) {
        if (mr == null) return new ExpertTodayCaseItem.MedicalRecordSnapshot("", "");
        return new ExpertTodayCaseItem.MedicalRecordSnapshot(
                mr.getExpertNote() == null ? "" : mr.getExpertNote(),
                mr.getExpertAdvise() == null ? "" : mr.getExpertAdvise()
        );
    }

//    public List<ExpertTodayCaseItem> getTodayCases(Long expertId) {
//        LocalDate today = LocalDate.now();
//        return appointmentRepository.findExpertToday(expertId, today)
//                .stream().map(this::toTodayItem).toList();
//    }
//
//    public ExpertAppointmentDetailResponse getDetail(Long expertId, Long appointmentId) {
//        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//
//        AvailabilitySlotEntity s = a.getSlot();
//        int durationMin = durationMinFromSlot(s);
//
//        return new ExpertAppointmentDetailResponse(
//                a.getId(),
//                a.getStatus(),
//                s.getDate().toString(),
//                hhmm(s.getStartTime()),
//                hhmm(s.getEndTime()),
//                durationMin,
//                a.getComplaint(),
//                a.getUserNote(),
//                toPet(a.getPet()),
//                toOwner(a.getUser()),
//                toMedicalRecordSnapshot(a)
//        );
//    }
//
//    public JoinCallResponse joinCall(Long expertId, Long appointmentId) {
//        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//
//        if (a.getStatus() == 3) {
//            return endedJoin(a);
//        }
//
//        AvailabilitySlotEntity s = a.getSlot();
//        validateSlot(s);
//
//        LocalDate date = s.getDate();
//        LocalTime start = s.getStartTime();
//        LocalTime end = s.getEndTime();
//        int durationMin = durationMinFromSlot(s);
//
//        ZoneId zone = ZoneId.systemDefault();
//        Instant nowInstant = Instant.now();
//        Instant startInstant = ZonedDateTime.of(date, start, zone).toInstant();
//        Instant endInstant = ZonedDateTime.of(date, end, zone).toInstant();
//
//        long nowMs = nowInstant.toEpochMilli();
//        long startMs = startInstant.toEpochMilli();
//        long endMs = endInstant.toEpochMilli();
//
//        if (nowMs < startMs) {
//            int secondsToStart = (int) Math.max(0, (startMs - nowMs) / 1000);
//            return new JoinCallResponse(
//                    CallJoinState.WAITING,
//                    a.getId(),
//                    date.toString(),
//                    hhmm(start),
//                    hhmm(end),
//                    durationMin,
//                    nowMs, startMs, endMs,
//                    secondsToStart,
//                    null,
//                    null
//            );
//        }
//
//        if (nowMs > endMs) {
//            return new JoinCallResponse(
//                    CallJoinState.ENDED,
//                    a.getId(),
//                    date.toString(),
//                    hhmm(start),
//                    hhmm(end),
//                    durationMin,
//                    nowMs, startMs, endMs,
//                    null,
//                    0,
//                    null
//            );
//        }
//
//        int remainingSec = (int) Math.max(0, (endMs - nowMs) / 1000);
//
//        // ✅ IMPORTANT: roomName must match user side exactly
//        String roomName = "appt_" + a.getId();
//        String joinUrl = jitsiDomain + "/" + roomName;
//
//        JitsiCallInfo jitsi = new JitsiCallInfo(roomName, joinUrl, null);
//
//        return new JoinCallResponse(
//                CallJoinState.ACTIVE,
//                a.getId(),
//                date.toString(),
//                hhmm(start),
//                hhmm(end),
//                durationMin,
//                nowMs, startMs, endMs,
//                null,
//                remainingSec,
//                jitsi
//        );
//    }
//
//    @Transactional
//    public ExpertTodayCaseItem.MedicalRecordSnapshot saveMedicalRecord(Long expertId, Long appointmentId, SaveMedicalRecordRequest req) {
//        AppointmentEntity a = appointmentRepository.findDetailForExpert(appointmentId, expertId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found"));
//
//        // (optional) validate petId matches appointment pet
//        if (req.petId() != null && a.getPet() != null && !a.getPet().getId().equals(req.petId())) {
//            throw new IllegalArgumentException("Pet mismatch");
//        }
//
//        MedicalRecordEntity mr = medicalRecordRepository.findByAppointment_Id(appointmentId)
//                .orElseGet(() -> {
//                    MedicalRecordEntity x = new MedicalRecordEntity();
//                    x.setAppointment(a);
//                    x.setPet(a.getPet());
//                    // x.setUser(a.getUser()); // nếu entity bạn có field này
//                    // x.setExpert(a.getExpert());
//                    return x;
//                });
//
//        mr.setExpertNote(req.expertNote());
//        mr.setExpertAdvise(req.expertAdvise());
//
//        medicalRecordRepository.save(mr);
//
//        return new ExpertTodayCaseItem.MedicalRecordSnapshot(mr.getExpertNote(), mr.getExpertAdvise());
//    }
//
//    // ===== helpers =====
//
//    private void validateSlot(SlotEntity s) {
//        if (s == null || s.getDate() == null || s.getStartTime() == null || s.getEndTime() == null) {
//            throw new IllegalStateException("Slot time invalid");
//        }
//    }
//
//    private int durationMinFromSlot(SlotEntity s) {
//        long minutes = Duration.between(s.getStartTime(), s.getEndTime()).toMinutes();
//        return (int) Math.max(0, minutes);
//    }
//
//    private String hhmm(LocalTime t) {
//        return t == null ? "" : t.toString().substring(0, 5);
//    }
//
//    private JoinCallResponse endedJoin(AppointmentEntity a) {
//        SlotEntity s = a.getSlot();
//        ZoneId zone = ZoneId.systemDefault();
//        Instant now = Instant.now();
//        Instant start = ZonedDateTime.of(s.getDate(), s.getStartTime(), zone).toInstant();
//        Instant end = ZonedDateTime.of(s.getDate(), s.getEndTime(), zone).toInstant();
//
//        return new JoinCallResponse(
//                CallJoinState.ENDED,
//                a.getId(),
//                s.getDate().toString(),
//                hhmm(s.getStartTime()),
//                hhmm(s.getEndTime()),
//                durationMinFromSlot(s),
//                now.toEpochMilli(),
//                start.toEpochMilli(),
//                end.toEpochMilli(),
//                null,
//                0,
//                null
//        );
//    }
//
//    private ExpertTodayCaseItem toTodayItem(AppointmentEntity a) {
//        AvailabilitySlotEntity s = a.getSlot();
//        return new ExpertTodayCaseItem(
//                a.getId(),
//                s.getDate().toString(),
//                hhmm(s.getStartTime()),
//                hhmm(s.getEndTime()),
//                durationMinFromSlot(s),
//                a.getStatus(),
//                a.getComplaint(),
//                a.getUserNote(),
//                toPet(a.getPet()),
//                toOwner(a.getUser()),
//                toMedicalRecordSnapshot(a)
//        );
//    }
//
//    private ExpertTodayCaseItem.PetSnapshot toPet(PetEntity p) {
//        if (p == null) return null;
//        return new ExpertTodayCaseItem.PetSnapshot(
//                p.getId(),
//                p.getName(),
//                p.getBreed(),
//                p.getGender(),
//                p.getDateOfBirth() == null ? null : p.getDateOfBirth().toString(),
//                p.getImageUrl(),
//                p.getDescription(),
//                p.getMicrochipId(),
//                p.getAllergies(),
//                p.getChronicDiseases(),
//                p.getWeight()
//        );
//    }
//
//    private ExpertTodayCaseItem.OwnerSnapshot toOwner(UserEntity u) {
//        if (u == null) return null;
//        return new ExpertTodayCaseItem.OwnerSnapshot(u.getId(), u.getFullName(), u.getPhone());
//    }
//
//    private ExpertTodayCaseItem.MedicalRecordSnapshot toMedicalRecordSnapshot(AppointmentEntity a) {
//        MedicalRecordEntity mr = a.getMedicalRecord(); // nếu mapping OneToOne
//        if (mr == null) return new ExpertTodayCaseItem.MedicalRecordSnapshot("", "");
//        return new ExpertTodayCaseItem.MedicalRecordSnapshot(
//                mr.getExpertNote() == null ? "" : mr.getExpertNote(),
//                mr.getExpertAdvise() == null ? "" : mr.getExpertAdvise()
//        );
//    }
}

