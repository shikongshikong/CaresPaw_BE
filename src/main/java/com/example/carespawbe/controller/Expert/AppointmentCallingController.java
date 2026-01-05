package com.example.carespawbe.controller.Expert;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.service.Expert.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calling/appointments")
public class AppointmentCallingController {

    private final AppointmentService appointmentService;

    public AppointmentCallingController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /api/user/appointments?page=0&size=20
    @GetMapping("")
    public List<AppointmentListItemResponse> getMyAppointments(
            @RequestParam(value = "type", required = false) String type,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return appointmentService.getMyAppointmentList(userId, type);
    }

    @PostMapping("/{id}/cancel")
    public void cancel(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        appointmentService.cancelAsUser(id, userId);
    }

    // for calling
    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getMyAppointmentDetail(
            @PathVariable Long appointmentId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        return ResponseEntity.ok(appointmentService.getMyAppointmentDetail(userId, appointmentId));
    }

    @PostMapping("/{appointmentId}/join")
    public ResponseEntity<?> joinCall(
            @PathVariable Long appointmentId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        return ResponseEntity.ok(appointmentService.joinCall(userId, appointmentId));
    }

    @PostMapping("{appointmentId}/end")
    public ResponseEntity<?> endCallAsUser(
            @PathVariable("appointmentId") Long appointmentId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId"); // tuỳ bạn set ở filter
        if (userId == null) return ResponseEntity.status(401).build();

        appointmentService.endCall(appointmentId, userId, null);
        return ResponseEntity.ok().build();
    }
}

