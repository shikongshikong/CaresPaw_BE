package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.videoCall.AppointmentDetailResponse;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.dto.Expert.videoCall.EndCallRequest;
import com.example.carespawbe.dto.Expert.videoCall.StartCallResponse;
import com.example.carespawbe.service.Expert.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expert/appointments")
public class ExpertCallingController {

    private final AppointmentService appointmentService;

    public ExpertCallingController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // GET /api/expert/appointments?page=0&size=20
    @GetMapping
    public Page<AppointmentListItemResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam Long expertId // TODO: remove when security ready
    ) {
        return appointmentService.listForExpert(expertId, page, size);
    }

    // GET /api/expert/appointments/{id}
    @GetMapping("/{id}")
    public AppointmentDetailResponse detail(
            @PathVariable Long id,
            @RequestParam Long expertId
    ) {
        return appointmentService.getDetailForExpert(id, expertId);
    }

    // POST /api/expert/appointments/{id}/call/start
    @PostMapping("/{id}/call/start")
    public StartCallResponse startCall(
            @PathVariable Long id,
            @RequestParam Long expertId
    ) {
        return appointmentService.startCallAsExpert(id, expertId);
    }

    // POST /api/expert/appointments/{id}/call/end
    @PostMapping("/{id}/call/end")
    public void endCall(
            @PathVariable Long id,
            @RequestParam Long expertId,
            @RequestBody EndCallRequest body
    ) {
        appointmentService.endCallAsExpert(id, expertId, body.markSuccess());
    }
}

