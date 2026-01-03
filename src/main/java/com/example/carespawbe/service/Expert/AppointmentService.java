package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Common.PagedResponse;
import com.example.carespawbe.dto.Expert.DashBoardStatisticItem;
import com.example.carespawbe.dto.Expert.ExpertAppListItem;
import com.example.carespawbe.dto.Expert.RemainApp;
import com.example.carespawbe.dto.Expert.UpComingApp;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentDetailResponse;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.dto.Expert.videoCall.StartCallResponse;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.PetEntity;
import com.example.carespawbe.enums.AppointmentStatus;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.service.Expert.videoCalling.JitsiMeetService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository  appointmentRepository;
    private final JitsiMeetService jitsiMeetService;

    public List<RemainApp> getTodayRemainingAppList(Long expertId) {
        List<RemainApp> remainAppList = new ArrayList<>();
        LocalDate  today = LocalDate.now();
        LocalTime  todayTime = LocalTime.now();

        List<AppointmentEntity> remainApp = appointmentRepository.findToday(expertId, today, todayTime);
        for (AppointmentEntity app : remainApp) {
            remainAppList.add(new RemainApp(app.getPet().getImageUrl(), app.getPet().getName(), app.getUser().getFullname(), app.getSlot().getStartTime(), app.getSlot().getEndTime(), app.getStatus()));
        }

        return remainAppList;
    }

    public UpComingApp getUpComingApp(Long expertId) {
        LocalDateTime now = LocalDateTime.now();
        Optional<AppointmentEntity> upEntity = appointmentRepository.findFirstByExpert_IdAndSlot_StartTimeAfterOrderBySlot_StartTimeAsc(expertId, now);
        if (upEntity.isPresent()) {
            AvailabilitySlotEntity slot = upEntity.get().getSlot();
            PetEntity pet = upEntity.get().getPet();
            return new UpComingApp(upEntity.get().getId(), slot.getDate(), slot.getStartTime(), slot.getEndTime(),
                    pet.getImageUrl(), pet.getName(), pet.getSpecies().getCategoryName(), pet.getBreed(),
                    pet.getUser().getFullname(), pet.getWeight(), upEntity.get().getUserNote());
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
public DashBoardStatisticItem<Long> getMonthlyAppointmentGrowth(Long expertId) {
    // 1. Xác định mốc thời gian (Tháng 1/2026 và Tháng 12/2025)
    YearMonth currentMonth = YearMonth.now();
    LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
    LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);

    YearMonth lastMonth = currentMonth.minusMonths(1);
    LocalDateTime startLast = lastMonth.atDay(1).atStartOfDay();
    LocalDateTime endLast = lastMonth.atEndOfMonth().atTime(23, 59, 59);

    // 2. Truy vấn số lượng CHÍNH XÁC theo chuyên gia
    long countThisMonth = appointmentRepository.countByExpert_IdAndSlot_StartTimeBetween(
            expertId, startCurrent, endCurrent);
    long countLastMonth = appointmentRepository.countByExpert_IdAndSlot_StartTimeBetween(
            expertId, startLast, endLast);

    // 3. Tính phần trăm tăng trưởng
    double growthRate = 0.0;
    if (countLastMonth > 0) {
        growthRate = ((double) (countThisMonth - countLastMonth) / countLastMonth) * 100;
    } else if (countThisMonth > 0) {
        growthRate = 100.0;
    }

    // 4. Trả về kết quả
    return DashBoardStatisticItem.of(countThisMonth, Math.round(growthRate * 100.0) / 100.0);
}


    public long getRemainingCount(Long expertId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = now.with(LocalTime.MAX); // 23:59:59.999
        int status = 0;

        return appointmentRepository.countByExpert_IdAndStatusAndSlot_StartTimeBetween(
                expertId,
                status, // Chỉ đếm những lịch sắp diễn ra
                now,
                endOfDay
        );
    }

    public DashBoardStatisticItem<Long> getMonthlyPetGrowth(Long expertId) {
        // 1. Xác định mốc thời gian (Tháng 1/2026 và Tháng 12/2025)
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startCurrent = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endCurrent = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        YearMonth lastMonth = currentMonth.minusMonths(1);
        LocalDateTime startLast = lastMonth.atDay(1).atStartOfDay();
        LocalDateTime endLast = lastMonth.atEndOfMonth().atTime(23, 59, 59);

        // 2. Truy vấn số lượng bệnh nhân (Pet) duy nhất
        long petsThisMonth = appointmentRepository.countDistinctPetsByExpertAndPeriod(
                expertId, startCurrent, endCurrent);
        long petsLastMonth = appointmentRepository.countDistinctPetsByExpertAndPeriod(
                expertId, startLast, endLast);

        // 3. Tính phần trăm tăng trưởng
        double growthRate = 0.0;
        if (petsLastMonth > 0) {
            growthRate = ((double) (petsThisMonth - petsLastMonth) / petsLastMonth) * 100;
        } else if (petsThisMonth > 0) {
            growthRate = 100.0;
        }

        // 4. Trả về đối tượng Generic
        return DashBoardStatisticItem.of(petsThisMonth, Math.round(growthRate * 100.0) / 100.0);
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

        Sort sortSpec = "oldest".equalsIgnoreCase(sort)
                ? Sort.by("date").ascending().and(Sort.by("time").ascending())
                : Sort.by("date").descending().and(Sort.by("time").descending());

        Pageable pageable = PageRequest.of(safePage, safeSize, sortSpec);

        Page<AppointmentEntity> pageData = appointmentRepository.findWithFilters(expertId, status, search, pageable);

        return PagedResponse.<ExpertAppListItem>builder()
                .items(pageData.getContent().stream().map(ExpertAppListItem::fromAppEntity).toList())
                .total(pageData.getTotalElements())
                .build();
    }

    @Transactional
    public void cancelByPublicId(Long appId) {
        AppointmentEntity appt = appointmentRepository.findById(appId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found: " + appId));

        // 3 = CANCELED
        if (appt.getStatus() == 3) return;
        appt.setStatus(3);
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

}
