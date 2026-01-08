package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Common.PagedResponse;
import com.example.carespawbe.dto.Expert.DashBoardStatisticItem;
import com.example.carespawbe.dto.Expert.ExpertAppListItem;
import com.example.carespawbe.dto.Expert.RemainApp;
import com.example.carespawbe.dto.Expert.UpComingApp;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentDetailResponse;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.dto.Expert.videoCall.JoinCallResponse;
import com.example.carespawbe.dto.Expert.videoCall.StartCallResponse;
import com.example.carespawbe.dto.Notification.NotificationCreateRequest;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.entity.Expert.PetEntity;
import com.example.carespawbe.enums.AppointmentStatus;
import com.example.carespawbe.enums.NotificationType;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.service.Expert.videoCalling.JitsiMeetService;
import com.example.carespawbe.service.Notification.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository  appointmentRepository;
    private final JitsiMeetService jitsiMeetService;

    @Autowired
    private NotificationService notificationService;

    public List<RemainApp> getTodayRemainingAppList(Long expertId) {
        LocalDate  today = LocalDate.now();
        LocalTime  todayTime = LocalTime.now();
        List<AppointmentEntity> remainApp = appointmentRepository.findToday(expertId, today, todayTime);

        List<RemainApp> remainAppList = new ArrayList<>();
        for (AppointmentEntity app : remainApp) {
            remainAppList.add(new RemainApp(app.getPet().getImageUrl(), app.getPet().getName(), app.getUser().getFullname(), app.getSlot().getStartTime(), app.getSlot().getEndTime(), app.getStatus()));
        }

        return remainAppList;
    }

    public UpComingApp getUpComingApp(Long expertId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        LocalTime endOfDay = LocalTime.of(23, 59, 59);
//        java.sql.Time now = java.sql.Time.valueOf(LocalTime.now());
//        java.sql.Time endOfDay = java.sql.Time.valueOf(LocalTime.of(23, 59, 59));
        List<AppointmentEntity> list = appointmentRepository.findUpcomingOrdered(expertId, today, now);
        if (list.isEmpty()) return null;

        AppointmentEntity upEntity = list.getFirst();
        if (upEntity != null) {
            AvailabilitySlotEntity slot = upEntity.getSlot();
            PetEntity pet = upEntity.getPet();

            return new UpComingApp(upEntity.getId(), slot.getDate(), slot.getStartTime(), slot.getEndTime(),
                    pet.getImageUrl(), pet.getName(), pet.getSpecies().getName(), pet.getBreed(),
                    pet.getUser().getFullname(), pet.getWeight(), upEntity.getUserNote(), upEntity.getStatus());
        }
        return null;
    }

//    public DashBoardStatisticItem getMonthlyGrowth() {
//        // 1. Xác định mốc thời gian tháng này (01/2026)
//        YearMonth currentMonth = YearMonth.now();
//        LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
//        LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        // 2. Xác định mốc thời gian tháng trước (12/2025)
//        YearMonth lastMonth = currentMonth.minusMonths(1);
//        LocalDateTime startLast = lastMonth.atDay(1).atStartOfDay();
//        LocalDateTime endLast = lastMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        // 3. Truy vấn số lượng
//        long countThisMonth = appointmentRepository.countBySlot_StartTimeBetween(startCurrent, endCurrent);
//        long countLastMonth = appointmentRepository.countBySlot_StartTimeBetween(startLast, endLast);
//
//        // 4. Tính phần trăm tăng trưởng
//        double growthRate = 0.0;
//        if (countLastMonth > 0) {
//            growthRate = ((double) (countThisMonth - countLastMonth) / countLastMonth) * 100;
//        } else if (countThisMonth > 0) {
//            growthRate = 100.0; // Nếu tháng trước bằng 0 và tháng này có lịch, tăng trưởng 100%
//        }
//
//        DashBoardStatisticItem appTotal = new DashBoardStatisticItem(countThisMonth, Math.round(growthRate * 100.0) / 100.0);
//
//        return appTotal;
//    }
    private static double growthRate(long current, long last) {
        if (last > 0) return ((double) (current - last) / last) * 100.0;
        if (current > 0) return 100.0;
        return 0.0;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public DashBoardStatisticItem<Long> getMonthlyAppointmentGrowth(Long expertId) {
    //    YearMonth currentMonth = YearMonth.now();
    //    LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
    //    LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);
    //
    //    YearMonth lastMonth = currentMonth.minusMonths(1);
    //    LocalDateTime startLast = lastMonth.atDay(1).atStartOfDay();
    //    LocalDateTime endLast = lastMonth.atEndOfMonth().atTime(23, 59, 59);
    //
    //    long countThisMonth = appointmentRepository.countByExpert_IdAndSlot_StartTimeBetween(
    //            expertId, startCurrent, endCurrent);
    //    long countLastMonth = appointmentRepository.countByExpert_IdAndSlot_StartTimeBetween(
    //            expertId, startLast, endLast);
    //
    //    double growthRate = 0.0;
    //    if (countLastMonth > 0) {
    //        growthRate = ((double) (countThisMonth - countLastMonth) / countLastMonth) * 100;
    //    } else if (countThisMonth > 0) {
    //        growthRate = 100.0;
    //    }
    //
    //    return DashBoardStatisticItem.of(countThisMonth, Math.round(growthRate * 100.0) / 100.0);
        YearMonth currentMonth = YearMonth.now();
        LocalDate startCurrent = currentMonth.atDay(1);
        LocalDate endCurrent = currentMonth.atEndOfMonth();

        YearMonth lastMonth = currentMonth.minusMonths(1);
        LocalDate startLast = lastMonth.atDay(1);
        LocalDate endLast = lastMonth.atEndOfMonth();

        long thisMonth = appointmentRepository.countAppointmentsInDateRange(expertId, startCurrent, endCurrent);
        long last = appointmentRepository.countAppointmentsInDateRange(expertId, startLast, endLast);

        double growth = growthRate(thisMonth, last);
        return DashBoardStatisticItem.of(thisMonth, round2(growth));
    }


    public Long getRemainingCount(Long expertId) {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime endOfDay = now.with(LocalTime.MAX); // 23:59:59.999
//        int status = 0;
//
//        return appointmentRepository.countByExpert_IdAndStatusAndSlot_StartTimeBetween(
//                expertId,
//                status,
//                now,
//                endOfDay
//        );
        return appointmentRepository.countRemainingToday(expertId, LocalDate.now(), LocalTime.now());
    }

    public DashBoardStatisticItem<Long> getMonthlyPetGrowth(Long expertId) {
//        YearMonth currentMonth = YearMonth.now();
//        LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
//        LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        YearMonth lastMonth = currentMonth.minusMonths(1);
//        LocalDateTime startLast = lastMonth.atDay(1).atStartOfDay();
//        LocalDateTime endLast = lastMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        long petsThisMonth = appointmentRepository.countDistinctPetsByExpertAndPeriod(
//                expertId, startCurrent, endCurrent);
//        long petsLastMonth = appointmentRepository.countDistinctPetsByExpertAndPeriod(
//                expertId, startLast, endLast);
//
//        double growthRate = 0.0;
//        if (petsLastMonth > 0) {
//            growthRate = ((double) (petsThisMonth - petsLastMonth) / petsLastMonth) * 100;
//        } else if (petsThisMonth > 0) {
//            growthRate = 100.0;
//        }
//
//        return DashBoardStatisticItem.of(petsThisMonth, Math.round(growthRate * 100.0) / 100.0);
        YearMonth currentMonth = YearMonth.now();
        LocalDate startCurrent = currentMonth.atDay(1);
        LocalDate endCurrent = currentMonth.atEndOfMonth();

        YearMonth lastMonth = currentMonth.minusMonths(1);
        LocalDate startLast = lastMonth.atDay(1);
        LocalDate endLast = lastMonth.atEndOfMonth();

        long thisMonth = appointmentRepository.countDistinctPetsInDateRange(expertId, startCurrent, endCurrent);
        long last = appointmentRepository.countDistinctPetsInDateRange(expertId, startLast, endLast);

        double growth = growthRate(thisMonth, last);
        return DashBoardStatisticItem.of(thisMonth, round2(growth));
    }

    public PagedResponse<ExpertAppListItem> getAppointments(
            Long expertId,
            int page, int pageSize,
            String search,
            Integer status,          // ✅ status là số
            String sort              // newest|oldest
    ) {
        int safePage = Math.max(page, 1) - 1;               // FE 1-based -> BE 0-based
        int safeSize = Math.min(Math.max(pageSize, 1), 100);

//        Sort sortSpec = "oldest".equalsIgnoreCase(sort)
//                ? Sort.by("date").ascending().and(Sort.by("time").ascending())
//                : Sort.by("date").descending().and(Sort.by("time").descending());
        Sort sortSpec = "oldest".equalsIgnoreCase(sort)
                ? Sort.by("slot.date").ascending().and(Sort.by("slot.startTime").ascending())
                : Sort.by("slot.date").descending().and(Sort.by("slot.startTime").descending());

        Pageable pageable = PageRequest.of(safePage, safeSize, sortSpec);

        Page<AppointmentEntity> pageData = appointmentRepository.findWithFilters(expertId, status, search, pageable);

        return PagedResponse.<ExpertAppListItem>builder()
                .items(pageData.getContent().stream().map(ExpertAppListItem::fromAppEntity).toList())
                .total(pageData.getTotalElements())
                .build();
    }

//    @Transactional
//    public void cancelByPublicId(Long appId) {
//        AppointmentEntity appt = appointmentRepository.findById(appId)
//                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appId));
//
//        // 3 = CANCELED
//        if (appt.getStatus() == 3) return;
//        appt.setStatus(3);
//    }

    @Transactional
    public void cancelByPublicId(Long appId) {
        AppointmentEntity appt = appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appId));

        Integer st = appt.getStatus() == null ? 0 : appt.getStatus(); // null -> PENDING

        // already canceled -> idempotent
        if (st == 3) return;

        // không cho hủy khi đã hoàn thành
        if (st == 2) {
            throw new IllegalStateException("Cannot cancel a completed appointment: " + appId);
        }

        // nếu bạn muốn cứng hơn: đang PROGRESS thì cũng không cho hủy
        // if (st == 1) {
        //     throw new IllegalStateException("Cannot cancel an in-progress appointment: " + appId);
        // }

        appt.setStatus(3);

        // rõ ràng và an toàn (dù transactional có thể tự flush)
        appointmentRepository.save(appt);

        // ===== NOTIFY EXPERT =====
        if (appt.getExpert() != null && appt.getExpert().getUser() != null && appt.getExpert().getUser().getId() != null) {
            notificationService.create(NotificationCreateRequest.builder()
                    .userId(appt.getExpert().getUser().getId()) // expert nhận
                    .actorId(appt.getUser() != null ? appt.getUser().getId() : null) // user là người hủy
                    .type(NotificationType.EXPERT)
                    .title("Appointment cancelled")
                    .message(appt.getUser() != null
                            ? (appt.getUser().getFullname() + " cancelled an appointment.")
                            : "A user cancelled an appointment.")
                    .link("/expert/appointments/" + appt.getId())
                    .entityType("APPOINTMENT")
                    .entityId(appt.getId())
                    .build());
        }

    }


    // calling by jitsi
    public AppointmentService(AppointmentRepository appointmentRepository, JitsiMeetService jitsiMeetService) {
        this.appointmentRepository = appointmentRepository;
        this.jitsiMeetService = jitsiMeetService;
    }

    // ========== LIST ==========
    @Transactional()
    public Page<AppointmentListItemResponse> listForUser(Long userId, int page, int size) {
        Page<AppointmentEntity> p = appointmentRepository.findByUserId(userId, PageRequest.of(page, size));
        for (AppointmentEntity entity : p.getContent()) {
            System.out.println("status: " + entity.getStatus());
        }
        return p.map(this::toListItem);
    }

//    @Transactional(readOnly = true)
    @Transactional()
    public Page<AppointmentListItemResponse> listForExpert(Long expertId, int page, int size) {
        Page<AppointmentEntity> p = appointmentRepository.findByExpertId(expertId, PageRequest.of(page, size));
        return p.map(this::toListItem);
    }

    // ========== DETAIL ==========
//    @Transactional(readOnly = true)
    @Transactional()
    public AppointmentDetailResponse getDetailForUser(Long appointmentId, Long userId) {
        AppointmentEntity a = appointmentRepository.findOwnedByUser(appointmentId, userId)
                .flatMap(x -> appointmentRepository.findDetailById(x.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by user"));
        return toDetail(a, "user");
    }

//    @Transactional(readOnly = true)
    @Transactional()
    public AppointmentDetailResponse getDetailForExpert(Long appointmentId, Long expertId) {
        AppointmentEntity a = appointmentRepository.findOwnedByExpert(appointmentId, expertId)
                .flatMap(x -> appointmentRepository.findDetailById(x.getId()))
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by expert"));
        return toDetail(a, "expert");
    }

    // ========== CALL ACTIONS ==========
    @Transactional
    public StartCallResponse startCallAsExpert(Long appointmentId, Long expertId) {
        AppointmentEntity a = appointmentRepository.findOwnedByExpert(appointmentId, expertId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by expert"));

        if (a.getStatus() == AppointmentStatus.CANCELED || a.getStatus() == AppointmentStatus.SUCCESS) {
            throw new IllegalStateException("Appointment already ended.");
        }

        // set status -> PROGRESS
        a.setStatus(AppointmentStatus.PROGRESS);
        appointmentRepository.save(a);

        // ===== NOTIFY USER =====
        if (a.getUser() != null && a.getUser().getId() != null) {
            Long expertUserId = (a.getExpert() != null && a.getExpert().getUser() != null) ? a.getExpert().getUser().getId() : null;

            notificationService.create(NotificationCreateRequest.builder()
                    .userId(a.getUser().getId())                 // user nhận
                    .actorId(expertUserId)                       // expert là người thực hiện
                    .type(NotificationType.EXPERT)
                    .title("Your appointment is starting")
                    .message(a.getExpert() != null ? (a.getExpert().getFullName() + " started the call.") : "Expert started the call.")
                    .link("/user/appointments/" + a.getId())
                    .entityType("APPOINTMENT")
                    .entityId(a.getId())
                    .build());
        }


        String roomName = jitsiMeetService.buildRoomName(a.getId());
        String joinUrl = jitsiMeetService.buildJoinUrl(roomName);
        String jwt = jitsiMeetService.maybeGenerateJwt(roomName, a.getExpert().getFullName(), "expert");

        return new StartCallResponse(a.getId(), a.getStatus(), roomName, joinUrl, jwt);
    }

    @Transactional
    public StartCallResponse joinCallAsUser(Long appointmentId, Long userId) {
        AppointmentEntity a = appointmentRepository.findOwnedByUser(appointmentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by user"));

        if (a.getStatus() == AppointmentStatus.CANCELED || a.getStatus() == AppointmentStatus.SUCCESS) {
            throw new IllegalStateException("Appointment already ended.");
        }

        // User join call: không bắt buộc set PROGRESS ở đây
        // Nếu bạn muốn: chỉ set PROGRESS khi expert start call.
        String roomName = jitsiMeetService.buildRoomName(a.getId());
        String joinUrl = jitsiMeetService.buildJoinUrl(roomName);
        String jwt = jitsiMeetService.maybeGenerateJwt(roomName, a.getUser().getFullname(), "user");

        return new StartCallResponse(a.getId(), a.getStatus(), roomName, joinUrl, jwt);
    }

    @Transactional
    public void endCallAsExpert(Long appointmentId, Long expertId, boolean markSuccess) {
        AppointmentEntity a = appointmentRepository.findOwnedByExpert(appointmentId, expertId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by expert"));

        if (a.getStatus() == AppointmentStatus.CANCELED || a.getStatus() == AppointmentStatus.SUCCESS) {
            return; // idempotent
        }

        if (markSuccess) a.setStatus(AppointmentStatus.SUCCESS);
        else a.setStatus(AppointmentStatus.CANCELED); // hoặc giữ PROGRESS tuỳ nghiệp vụ

        appointmentRepository.save(a);

        // ===== NOTIFY USER =====
        if (a.getUser() != null && a.getUser().getId() != null) {
            Long expertUserId = (a.getExpert() != null && a.getExpert().getUser() != null) ? a.getExpert().getUser().getId() : null;

            notificationService.create(NotificationCreateRequest.builder()
                    .userId(a.getUser().getId())
                    .actorId(expertUserId)
                    .type(NotificationType.EXPERT)
                    .title("Appointment finished")
                    .message(markSuccess ? "Your appointment has been completed." : "Your appointment has been cancelled.")
                    .link("/user/appointments/" + a.getId())
                    .entityType("APPOINTMENT")
                    .entityId(a.getId())
                    .build());
        }

    }

    @Transactional
    public void cancelAsUser(Long appointmentId, Long userId) {
        AppointmentEntity a = appointmentRepository.findOwnedByUser(appointmentId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Appointment not found or not owned by user"));

        if (a.getStatus() == AppointmentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot cancel a completed appointment.");
        }

        a.setStatus(AppointmentStatus.CANCELED);
        appointmentRepository.save(a);

        // nếu cần: slot.booked = CANCEL hoặc EMPTY tuỳ nghiệp vụ cancel của bạn
        // (bạn có booked=2 cancel, nên cứ set slot.booked = 2 hoặc trả lại EMPTY nếu allow reuse)
    }

    // ========== MAPPERS ==========
    private AppointmentListItemResponse toListItem(AppointmentEntity a) {
        var s = a.getSlot();
        var p = a.getPet();
        var u = a.getUser();
        var e = a.getExpert();

        return new AppointmentListItemResponse(
                a.getId(),
                a.getStatus(),
                a.getPrice(),
                a.getUserNote(),
                s != null ? s.getId() : null,
                s != null ? s.getDate() : null,
                s != null ? s.getStartTime() : null,
                s != null ? s.getEndTime() : null,
                p != null ? p.getId() : null,
                p != null ? p.getName() : null,
                p != null ? p.getBreed() : null,
                p != null ? p.getGender() : null,
                p != null ? p.getImageUrl() : null,
                u != null ? u.getId() : null,
                u != null ? u.getFullname() : null,
                e != null ? e.getId() : null,
                e != null ? e.getFullName() : null
        );
    }

    private AppointmentDetailResponse toDetail(AppointmentEntity a, String viewerRole) {
        var s = a.getSlot();
        var p = a.getPet();
        var u = a.getUser();
        var e = a.getExpert();

        String roomName = jitsiMeetService.buildRoomName(a.getId());
        String joinUrl = jitsiMeetService.buildJoinUrl(roomName);

        // JWT optional:
        String displayName = "user".equals(viewerRole) ? (u != null ? u.getFullname() : "User") : (e != null ? e.getFullName() : "Expert");
        String jwt = jitsiMeetService.maybeGenerateJwt(roomName, displayName, viewerRole);

        var petSnapshot = new AppointmentDetailResponse.PetSnapshot(
                p != null ? p.getId() : null,
                p != null ? p.getName() : null,
                p != null ? p.getBreed() : null,
                p != null ? p.getGender() : null,
                p != null && p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null,
                p != null ? p.getImageUrl() : null,
                p != null ? p.getDescription() : null,
                p != null ? p.getMicrochipId() : null,
                p != null ? p.getAllergies() : null,
                p != null ? p.getChronic_diseases() : null,
                p != null ? p.getWeight() : null
        );

        var userSnapshot = new AppointmentDetailResponse.UserSnapshot(
                u != null ? u.getId() : null,
                u != null ? u.getFullname() : null
        );

        var expertSnapshot = new AppointmentDetailResponse.ExpertSnapshot(
                e != null ? e.getId() : null,
                e != null ? e.getFullName() : null
        );

        var jitsi = new AppointmentDetailResponse.JitsiCallInfo(roomName, joinUrl, jwt);

        return new AppointmentDetailResponse(
                a.getId(),
                a.getStatus(),
                a.getPrice(),
                a.getUserNote(),
                s != null ? s.getId() : null,
                s != null ? s.getDate() : null,
                s != null ? s.getStartTime() : null,
                s != null ? s.getEndTime() : null,
                petSnapshot,
                userSnapshot,
                expertSnapshot,
                jitsi
        );


    }

    public List<AppointmentListItemResponse> getMyAppointments(Long userId, String type) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<AppointmentEntity> list;
        String t = (type == null) ? "upcoming" : type.toLowerCase();

        switch (t) {
            case "past" -> list = appointmentRepository.findPastForUser(userId, today, now);
            case "cancelled" -> list = appointmentRepository.findCancelledForUser(userId);
            case "all" -> {
                // simple combine
                List<AppointmentEntity> upcoming = appointmentRepository.findUpcomingForUser(userId, today, now);
                List<AppointmentEntity> past = appointmentRepository.findPastForUser(userId, today, now);
                List<AppointmentEntity> cancelled = appointmentRepository.findCancelledForUser(userId);
                upcoming.addAll(past);
                upcoming.addAll(cancelled);
                list = upcoming;
            }
            default -> list = appointmentRepository.findUpcomingForUser(userId, today, now);
        }

        return list.stream().map(this::toListItem).toList();
    }

    public AppointmentDetailResponse getMyAppointmentDetail(Long userId, Long appointmentId) {
        AppointmentEntity a = appointmentRepository.findDetailForUser(appointmentId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        return toDetail(a);
    }

//    public JoinCallResponse joinCall(Long userId, Long appointmentId) {
//        AppointmentEntity a = appointmentRepository.findDetailForUser(appointmentId, userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));
//
//        if (a.getStatus() != null && a.getStatus() == 3) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment is cancelled");
//        }
//
//        AvailabilitySlotEntity s = a.getSlot();
//        if (s == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing slot");
//
//        // Join window: start - 10m to end + 15m
//        LocalDate date = s.getDate();
//        LocalTime start = s.getStartTime();
//        LocalTime end = s.getEndTime();
//        if (date == null || start == null || end == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid schedule");
//        }
//
//        LocalDateTime startDt = LocalDateTime.of(date, start);
//        LocalDateTime endDt = LocalDateTime.of(date, end);
//
//        LocalDateTime windowStart = startDt.minusMinutes(10);
//        LocalDateTime windowEnd = endDt.plusMinutes(15);
//
//        LocalDateTime now = LocalDateTime.now();
//        if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "Join is only available near the appointment time"
//            );
//        }
//
//        // roomId derive theo appointmentId
//        String roomId = "app_" + a.getId();
//
//        // Nếu bạn có hệ thống call riêng, có thể build joinUrl luôn:
//        // String joinUrl = "https://your-domain/call/" + roomId;
//        String joinUrl = null;
//
//        return new JoinCallResponse(roomId, joinUrl);
//    }

//    private AppointmentListItemResponse toListItem(AppointmentEntity a) {
//        AvailabilitySlotEntity s = a.getSlot();
//        ExpertEntity e = a.getExpert();
//        PetEntity p = a.getPet();
//
//        return new AppointmentListItemResponse(
//                a.getId(),
//                a.getStatus(),
//                s != null ? s.getDate() : null,
//                s != null ? s.getStartTime() : null,
//                s != null ? s.getEndTime() : null,
//                a.getPrice(),
//                a.getUserNote(),
//
//                e != null ? e.getId() : null,
//                e != null ? e.getFullName() : null,
//                e != null ? e.getIdImage() : null, // avatar
//
//                p != null ? p.getId() : null,
//                p != null ? p.getName() : null
//        );
//    }

//    private AppointmentDetailResponse toDetail(AppointmentEntity a) {
//        AvailabilitySlotEntity s = a.getSlot();
//        ExpertEntity e = a.getExpert();
//        PetEntity p = a.getPet();
//
//        var slotSnapshot = AppointmentDetailResponse.SlotSnapshot.builder()
//                .slotId(s != null ? s.getId() : null)
//                .date(s != null && s.getDate() != null ? s.getDate().toString() : null)
//                .startTime(s != null && s.getStartTime() != null ? s.getStartTime().toString() : null)
//                .endTime(s != null && s.getEndTime() != null ? s.getEndTime().toString() : null)
//                .price(s != null ? s.getPrice() : null)
//                .booked(s != null ? s.getBooked() : null)
//                .build();
//
//        var expertSnapshot = AppointmentDetailResponse.ExpertSnapshot.builder()
//                .expertId(e != null ? e.getId() : null)
//                .fullName(e != null ? e.getFullName() : null)
//                .avatar(e != null ? e.getIdImage() : null)
//                .build();
//
//        var petSnapshot = AppointmentDetailResponse.PetSnapshot.builder()
//                .id(p != null ? p.getId() : null)
//                .name(p != null ? p.getName() : null)
//                .breed(p != null ? p.getBreed() : null)
//                .gender(p != null ? p.getGender() : null)
//                .dateOfBirth(p != null && p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null)
//                .imageUrl(p != null ? p.getImageUrl() : null)
//                .description(p != null ? p.getDescription() : null)
//                .microchipId(p != null ? p.getMicrochipId() : null)
//                .allergies(p != null ? p.getAllergies() : null)
//                .chronicDiseases(p != null ? p.getChronic_diseases() : null)
//                .weight(p != null ? p.getWeight() : null)
//                .build();
//
//        return AppointmentDetailResponse.builder()
//                .appointmentId(a.getId())
//                .status(a.getStatus())
//                .userNote(a.getUserNote())
//                .price(a.getPrice())
//                .slot(slotSnapshot)
//                .expert(expertSnapshot)
//                .pet(petSnapshot)
//                .build();
//    }
//private AppointmentDetailResponse toDetail(AppointmentEntity a) {
//    AvailabilitySlotEntity s = a != null ? a.getSlot() : null;
//
//    // expert có thể lấy từ Appointment hoặc từ Slot (tuỳ entity bạn set ở đâu)
//    ExpertEntity e = null;
//    if (a != null && a.getExpert() != null) e = a.getExpert();
//    else if (s != null && s.getExpert() != null) e = s.getExpert();
//
//    UserEntity u = a != null ? a.getUser() : null;
//    PetEntity p = a != null ? a.getPet() : null;
//
//    // ===== pet snapshot =====
//    var petSnapshot = AppointmentDetailResponse.PetSnapshot.builder()
//            .id(p != null ? p.getId() : null)
//            .name(p != null ? p.getName() : null)
//            .breed(p != null ? p.getBreed() : null)
//            .gender(p != null ? p.getGender() : null)
//            .dateOfBirth(p != null && p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null)
//            .imageUrl(p != null ? p.getImageUrl() : null)
//            .description(p != null ? p.getDescription() : null)
//            .microchipId(p != null ? p.getMicrochipId() : null)
//            .allergies(p != null ? p.getAllergies() : null)
//            .chronicDiseases(p != null ? p.getChronic_diseases() : null)
//            .weight(p != null ? p.getWeight() : null)
//            .build();
//
//    // ===== user snapshot =====
//    var userSnapshot = AppointmentDetailResponse.UserSnapshot.builder()
//            .id(u != null ? u.getId() : null)
//            .fullName(u != null ? u.getFullname() : null) // chú ý: entity bạn là fullname
//            .build();
//
//    // ===== expert snapshot =====
//    var expertSnapshot = AppointmentDetailResponse.ExpertSnapshot.builder()
//            .id(e != null ? e.getId() : null)
//            .fullName(e != null ? e.getFullName() : null)
//            .build();
//
//    // ===== jitsi =====
//    // nếu bạn có @Value("${app.jitsi.domain:meet.jit.si}") private String jitsiDomain;
//    String roomName = "app_" + (a != null ? a.getId() : "unknown");
//    String domain = (jitsiDomain == null || jitsiDomain.isBlank())
//            ? "meet.jit.si"
//            : jitsiDomain.trim().replace("https://", "").replace("http://", "").replaceAll("/+$", "");
//    String joinUrl = "https://" + domain + "/" + roomName;
//
//    var jitsiInfo = new AppointmentDetailResponse.JitsiCallInfo(
//            roomName,
//            joinUrl,
//            null // jwt optional
//    );
//
//    return AppointmentDetailResponse.builder()
//            .appointmentId(a != null ? a.getId() : null)
//            .status(a != null ? a.getStatus() : null)
//            .price(a != null ? a.getPrice() : null)
//            .userNote(a != null ? a.getUserNote() : null)
//
//            .slotId(s != null ? s.getId() : null)
//            .date(s != null ? s.getDate() : null)
//            .startTime(s != null ? s.getStartTime() : null)
//            .endTime(s != null ? s.getEndTime() : null)
//
//            .pet(petSnapshot)
//            .user(userSnapshot)
//            .expert(expertSnapshot)
//
//            .jitsi(jitsiInfo)
//            .build();
//}
    @Value("${app.jitsi.domain:meet.jit.si}")
    private String jitsiDomain;
    private AppointmentDetailResponse toDetail(AppointmentEntity a) {
        AvailabilitySlotEntity s = (a != null) ? a.getSlot() : null;

        // expert: ưu tiên lấy từ Appointment, nếu null thì lấy từ slot
        ExpertEntity e = null;
        if (a != null && a.getExpert() != null) e = a.getExpert();
        else if (s != null && s.getExpert() != null) e = s.getExpert();

        UserEntity u = (a != null) ? a.getUser() : null;
        PetEntity p = (a != null) ? a.getPet() : null;

        // ===== Pet snapshot =====
        AppointmentDetailResponse.PetSnapshot petSnapshot =
                AppointmentDetailResponse.PetSnapshot.builder()
                        .id(p != null ? p.getId() : null)
                        .name(p != null ? p.getName() : null)
                        .breed(p != null ? p.getBreed() : null)
                        .gender(p != null ? p.getGender() : null)
                        .dateOfBirth(p != null && p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null)
                        .imageUrl(p != null ? p.getImageUrl() : null)
                        .description(p != null ? p.getDescription() : null)
                        .microchipId(p != null ? p.getMicrochipId() : null)
                        .allergies(p != null ? p.getAllergies() : null)
                        .chronicDiseases(p != null ? p.getChronic_diseases() : null)
                        .weight(p != null ? p.getWeight() : null)
                        .build();

        // ===== User snapshot =====
        AppointmentDetailResponse.UserSnapshot userSnapshot =
                AppointmentDetailResponse.UserSnapshot.builder()
                        .id(u != null ? u.getId() : null)
                        .fullName(u != null ? u.getFullname() : null) // nếu entity dùng getFullName() thì đổi ở đây
                        .build();

        // ===== Expert snapshot =====
        AppointmentDetailResponse.ExpertSnapshot expertSnapshot =
                AppointmentDetailResponse.ExpertSnapshot.builder()
                        .id(e != null ? e.getId() : null)
                        .fullName(e != null ? e.getFullName() : null)
                        .build();

        // ===== Jitsi info =====
        String roomName = "app_" + (a != null ? a.getId() : "unknown");

        String domain = (jitsiDomain == null || jitsiDomain.isBlank())
                ? "meet.jit.si"
                : jitsiDomain.trim()
                .replace("https://", "")
                .replace("http://", "")
                .replaceAll("/+$", "");

        String joinUrl = "https://" + domain + "/" + roomName;

        AppointmentDetailResponse.JitsiCallInfo jitsi =
                new AppointmentDetailResponse.JitsiCallInfo(roomName, joinUrl, null);

        // ===== Build response =====
        return AppointmentDetailResponse.builder()
                .appointmentId(a != null ? a.getId() : null)
                .status(a != null ? a.getStatus() : null)
                .price(a != null ? a.getPrice() : null)
                .userNote(a != null ? a.getUserNote() : null)

                .slotId(s != null ? s.getId() : null)
                .date(s != null ? s.getDate() : null)
                .startTime(s != null ? s.getStartTime() : null)
                .endTime(s != null ? s.getEndTime() : null)

                .pet(petSnapshot)
                .user(userSnapshot)
                .expert(expertSnapshot)

                .jitsi(jitsi)
                .build();
    }
}
