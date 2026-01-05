package com.example.carespawbe.service.Expert.videoCalling;

import com.example.carespawbe.config.JitsiProperties;
import com.example.carespawbe.dto.Expert.videoCall.JoinCallResponse;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class JitsiMeetService {

    private final JitsiProperties props;
    private final AppointmentRepository appointmentRepository;

    public JitsiMeetService(JitsiProperties props, AppointmentRepository appointmentRepository) {
        this.props = props;
        this.appointmentRepository = appointmentRepository;
    }

    public String buildRoomName(Long appointmentId) {
        // đảm bảo unique + khó đoán hơn (bạn có thể add random suffix)
        return "app-" + appointmentId;
    }

    public String buildJoinUrl(String roomName) {
        // encode phòng để an toàn
        String encoded = URLEncoder.encode(roomName, StandardCharsets.UTF_8);
        return props.getBaseUrl().replaceAll("/+$", "") + "/" + encoded;
    }

    public String maybeGenerateJwt(String roomName, String displayName, String role) {
        // OPTIONAL:
        // Nếu bạn dùng Jitsi JWT: generate token tại đây
        // Hiện tại return null cho đơn giản.
        return null;
    }

    @Value("${app.jitsi.domain:meet.jit.si}")
    private String jitsiDomain;

    public JoinCallResponse joinCall(Long userId, Long appointmentId) {
        AppointmentEntity a = appointmentRepository.findDetailForUser(appointmentId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found"));

        if (a.getStatus() != null && a.getStatus() == 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment is cancelled");
        }

        AvailabilitySlotEntity s = a.getSlot();
        if (s == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing slot");

        LocalDate date = s.getDate();
        LocalTime start = s.getStartTime();
        LocalTime end = s.getEndTime();

        if (date == null || start == null || end == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid schedule");
        }

        LocalDateTime startDt = LocalDateTime.of(date, start);
        LocalDateTime endDt = LocalDateTime.of(date, end);

        LocalDateTime windowStart = startDt.minusMinutes(10);
        LocalDateTime windowEnd = endDt.plusMinutes(15);

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(windowStart) || now.isAfter(windowEnd)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Join is only available near the appointment time");
        }

        // ✅ same room for user & expert
        String roomId = "app_" + a.getId();

        // ✅ normalize domain (no protocol)
        String domain = (jitsiDomain == null || jitsiDomain.isBlank()) ? "meet.jit.si" : jitsiDomain.trim()
                .replace("https://", "").replace("http://", "").replaceAll("/+$", "");

        String joinUrl = "https://" + domain + "/" + roomId;

        // optional displayName (user fullname)
        String displayName = (a.getUser() != null) ? a.getUser().getFullname() : "Guest";

        return new JoinCallResponse(roomId, domain, joinUrl, displayName);
    }
}

