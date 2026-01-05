package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.CalendarItemResponse;
import com.example.carespawbe.dto.Expert.CreateSlotRequest;
import com.example.carespawbe.dto.Expert.CreateSlotsResponse;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.entity.Expert.RecurrenceRuleEntity;
import com.example.carespawbe.enums.ConflictStrategy;
import com.example.carespawbe.enums.RecurrenceMode;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.repository.Expert.AvailabilitySlotRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import com.example.carespawbe.repository.Expert.RecurrenceRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertCalendarService {

    private final AvailabilitySlotRepository slotRepo;
    private final AppointmentRepository appointmentRepo;
    private final RecurrenceRuleRepository ruleRepo;
    private final ExpertRepository expertRepo;

    // chống “nổ” slot
    private static final int MAX_RECUR_DAYS = 60;

    /* ================= GET MONTH CALENDAR ================= */

    @Transactional(readOnly = true)
    public List<CalendarItemResponse> getMonthCalendar(Long expertId, YearMonth ym) {
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();

        List<AvailabilitySlotEntity> slots = slotRepo.findByExpert_IdAndDateBetween(expertId, from, to);
        List<AppointmentEntity> apps = appointmentRepo.findMonthAppointments(expertId, from, to);

        Map<Long, AppointmentEntity> appBySlotId = new HashMap<>();
        for (AppointmentEntity a : apps) {
            if (a.getSlot() != null) appBySlotId.put(a.getSlot().getId(), a);
        }

        List<CalendarItemResponse> items = new ArrayList<>();
        for (AvailabilitySlotEntity s : slots) {
            int startMin = toMin(s.getStartTime());
            int endMin = toMin(s.getEndTime());
            String dateKey = s.getDate().toString();

            AppointmentEntity a = appBySlotId.get(s.getId());
            if (a == null) {
                items.add(new CalendarItemResponse(
                        "slot_" + s.getId(),
                        dateKey,
                        startMin,
                        endMin,
                        "—",
                        "scheduled"
                ));
            } else {
                items.add(new CalendarItemResponse(
                        "app_" + a.getId(),
                        dateKey,
                        startMin,
                        endMin,
                        a.getUser() != null ? safeName(a.getUser().getFullname()) : "—",
                        mapAppointmentStatusToFe(a.getStatus())
                ));
            }
        }

        items.sort(Comparator
                .comparing(CalendarItemResponse::getDateKey)
                .thenComparingInt(CalendarItemResponse::getStartMin));

        return items;
    }

    /* ================= CREATE SLOTS WITH CONFLICT FLOW ================= */

    @Transactional
    public CreateSlotsResponse createSlotsWithRecurrence(Long expertId, CreateSlotRequest req) {
        ExpertEntity expert = expertRepo.findById(expertId)
                .orElseThrow(() -> new IllegalArgumentException("Expert not found"));

        LocalDate startDate = req.getDate();
        if (startDate == null) throw new IllegalArgumentException("date is required");

        LocalTime start = LocalTime.parse(req.getStartTime());
        int duration = Optional.ofNullable(req.getDurationMin()).orElse(60);
        LocalTime end = start.plusMinutes(duration);

        if (!end.isAfter(start)) throw new IllegalArgumentException("Invalid time range");

        var rp = req.getRecur();
        RecurrenceMode mode = parseMode(rp);
        LocalDate until = (rp != null && rp.getUntil() != null) ? rp.getUntil() : startDate;

        if (until.isBefore(startDate)) until = startDate;

        // limit recurring span
        if (until.isAfter(startDate.plusDays(MAX_RECUR_DAYS))) {
            throw new IllegalArgumentException("Apply-until too far. Max " + MAX_RECUR_DAYS + " days.");
        }

        List<LocalDate> dates = generateDates(startDate, until, mode, rp == null ? null : rp.getByWeekDays());
        int requested = dates.size();

        // load existing slots in [startDate..until] once
        List<AvailabilitySlotEntity> existing = slotRepo.findByExpert_IdAndDateBetween(expertId, startDate, until);
        Map<LocalDate, List<AvailabilitySlotEntity>> existingByDate = new HashMap<>();
        for (var s : existing) existingByDate.computeIfAbsent(s.getDate(), k -> new ArrayList<>()).add(s);

        // detect conflicts
        List<CreateSlotsResponse.SlotConflict> conflicts = new ArrayList<>();
        Set<LocalDate> conflictDates = new HashSet<>();
        Set<LocalDate> lockedDates = new HashSet<>();

        for (LocalDate d : dates) {
            boolean hasAny = false;
            boolean hasLocked = false;

            var daySlots = existingByDate.getOrDefault(d, List.of());
            for (var s : daySlots) {
                if (overlap(start, end, s.getStartTime(), s.getEndTime())) {
                    hasAny = true;

                    boolean overwritable = (s.getBooked() == null) || (s.getBooked() != 1);
                    String reason = overwritable ? "OVERLAP_WITH_EXISTING_SLOT" : "BOOKED_LOCKED";

                    if (!overwritable) hasLocked = true;

                    conflicts.add(new CreateSlotsResponse.SlotConflict(
                            d.toString(),
                            start.toString(),
                            end.toString(),
                            s.getId(),
                            s.getBooked(),
                            overwritable,
                            reason
                    ));
                }
            }

            if (hasAny) conflictDates.add(d);
            if (hasLocked) lockedDates.add(d);
        }

        ConflictStrategy strategy = req.getStrategy() != null ? req.getStrategy() : ConflictStrategy.ASK;
        boolean dryRun = Boolean.TRUE.equals(req.getDryRun()) || strategy == ConflictStrategy.ASK;

        if (dryRun) {
            int sk = conflictDates.size();
            return new CreateSlotsResponse(requested, 0, sk, List.of(), conflicts);
        }

        // Determine which dates to create
        LinkedHashSet<LocalDate> finalCreateDates = new LinkedHashSet<>();

        if (strategy == ConflictStrategy.SKIP) {
            // only dates without any conflict
            for (LocalDate d : dates) {
                if (!conflictDates.contains(d)) finalCreateDates.add(d);
            }
        } else if (strategy == ConflictStrategy.OVERWRITE) {
            // Cannot overwrite locked/booked occurrences
            for (LocalDate d : dates) {
                if (!lockedDates.contains(d)) finalCreateDates.add(d);
            }

            // delete overwritable conflicting slots (booked != 1)
            Set<Long> deletableSlotIds = conflicts.stream()
                    .filter(CreateSlotsResponse.SlotConflict::isOverwritable)
                    .map(CreateSlotsResponse.SlotConflict::getExistingSlotId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!deletableSlotIds.isEmpty()) {
                // Safe delete assumption: booked==1 means has appointment. booked!=1 ok.
                // If your DB allows appointment even when booked!=1, then you MUST check appointment before delete.
                slotRepo.deleteAllById(deletableSlotIds);
            }
        } else {
            // fallback
            for (LocalDate d : dates) {
                if (!conflictDates.contains(d)) finalCreateDates.add(d);
            }
        }

        int skipped = requested - finalCreateDates.size();

        // create rule record (if recurring)
        RecurrenceRuleEntity rule = null;
        if (mode != RecurrenceMode.NONE) {
            rule = new RecurrenceRuleEntity();
            rule.setMode(mode);
            rule.setUntil(until);
            if (mode == RecurrenceMode.WEEKLY && rp != null && rp.getByWeekDays() != null) {
                rule.setByWeekDays(String.join(",", rp.getByWeekDays()));
            }
            rule = ruleRepo.save(rule);
        }

        BigDecimal price = req.getPrice() != null ? req.getPrice() : expert.getSessionPrice();

        List<AvailabilitySlotEntity> toSave = new ArrayList<>();
        for (LocalDate d : finalCreateDates) {
            // avoid exact duplicates
            if (slotRepo.existsByExpert_IdAndDateAndStartTimeAndEndTime(expertId, d, start, end)) continue;

            AvailabilitySlotEntity s = new AvailabilitySlotEntity();
            s.setExpert(expert);
            s.setDate(d);
            s.setStartTime(start);
            s.setEndTime(end);
            s.setPrice(price);
            s.setBooked(0);
            if (rule != null) s.setRecurrenceRule(rule);

            toSave.add(s);
        }

        List<AvailabilitySlotEntity> saved = slotRepo.saveAll(toSave);

        if (rule != null && !saved.isEmpty()) {
            rule.setRootSlot(saved.get(0));
            ruleRepo.save(rule);
        }

        List<Long> ids = saved.stream().map(AvailabilitySlotEntity::getId).toList();
        return new CreateSlotsResponse(requested, saved.size(), skipped, ids, conflicts);
    }

    /* ================= helpers ================= */

    private static int toMin(LocalTime t) {
        return t.getHour() * 60 + t.getMinute();
    }

    private static String safeName(String s) {
        return (s == null || s.isBlank()) ? "—" : s.trim();
    }

    private static String mapAppointmentStatusToFe(Integer status) {
        if (status == null) return "pending";
        return switch (status) {
            case 0 -> "pending";      // pending
            case 1 -> "in_progress";  // progress
            case 2 -> "completed";    // success
            case 3 -> "cancelled";    // canceled
            default -> "pending";
        };
    }

    private static RecurrenceMode parseMode(CreateSlotRequest.RecurrencePayload rp) {
        if (rp == null || rp.getMode() == null) return RecurrenceMode.NONE;
        return switch (rp.getMode().toLowerCase()) {
            case "daily" -> RecurrenceMode.DAILY;
            case "weekly" -> RecurrenceMode.WEEKLY;
            case "monthly" -> RecurrenceMode.MONTHLY;
            default -> RecurrenceMode.NONE; // "none"
        };
    }

    private static boolean overlap(LocalTime aStart, LocalTime aEnd, LocalTime bStart, LocalTime bEnd) {
        return aStart.isBefore(bEnd) && aEnd.isAfter(bStart);
    }

    private static List<LocalDate> generateDates(LocalDate start, LocalDate until,
                                                 RecurrenceMode mode, List<String> byWeekDays) {
        List<LocalDate> res = new ArrayList<>();

        switch (mode) {
            case NONE -> res.add(start);

            case DAILY -> {
                for (LocalDate d = start; !d.isAfter(until); d = d.plusDays(1)) res.add(d);
            }

            case WEEKLY -> {
                Set<DayOfWeek> allow = parseWeekdays(byWeekDays);
                for (LocalDate d = start; !d.isAfter(until); d = d.plusDays(1)) {
                    if (allow.contains(d.getDayOfWeek())) res.add(d);
                }
                if (res.isEmpty()) res.add(start);
            }

            case MONTHLY -> {
                int dom = start.getDayOfMonth();
                LocalDate d = start;
                while (!d.isAfter(until)) {
                    res.add(d);
                    YearMonth nextYm = YearMonth.from(d).plusMonths(1);
                    int nextDom = Math.min(dom, nextYm.lengthOfMonth());
                    d = nextYm.atDay(nextDom);
                }
            }
        }
        return res;
    }

    private static Set<DayOfWeek> parseWeekdays(List<String> byWeekDays) {
        if (byWeekDays == null || byWeekDays.isEmpty()) return Set.of(DayOfWeek.MONDAY);

        Set<DayOfWeek> set = new HashSet<>();
        for (String k : byWeekDays) {
            if (k == null) continue;
            switch (k.toUpperCase()) {
                case "MO" -> set.add(DayOfWeek.MONDAY);
                case "TU" -> set.add(DayOfWeek.TUESDAY);
                case "WE" -> set.add(DayOfWeek.WEDNESDAY);
                case "TH" -> set.add(DayOfWeek.THURSDAY);
                case "FR" -> set.add(DayOfWeek.FRIDAY);
                case "SA" -> set.add(DayOfWeek.SATURDAY);
                case "SU" -> set.add(DayOfWeek.SUNDAY);
            }
        }
        return set.isEmpty() ? Set.of(DayOfWeek.MONDAY) : set;
    }
}


