package com.example.carespawbe.controller.Expert;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentDetailResponse;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.dto.Expert.videoCall.StartCallResponse;
import com.example.carespawbe.service.Expert.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

// ⚠️ bạn thay cách lấy userId theo security của bạn
@RestController
@RequestMapping("/api/user/appointments")
public class AppointmentCallingController {

    private final AppointmentService appointmentService;

    public AppointmentCallingController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /api/user/appointments?page=0&size=20
    @GetMapping
    public Page<AppointmentListItemResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Long userId // TODO: remove when security ready
    ) {
        return appointmentService.listForUser(userId, page, size);
    }

    // GET /api/user/appointments/{id}
    @GetMapping("/{id}")
    public AppointmentDetailResponse detail(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return appointmentService.getDetailForUser(id, userId);
    }

    // POST /api/user/appointments/{id}/call/join
    @PostMapping("/{id}/call/join")
    public StartCallResponse joinCall(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return appointmentService.joinCallAsUser(id, userId);
    }

    // POST /api/user/appointments/{id}/cancel
    @PostMapping("/{id}/cancel")
    public void cancel(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        appointmentService.cancelAsUser(id, userId);
    }
}

