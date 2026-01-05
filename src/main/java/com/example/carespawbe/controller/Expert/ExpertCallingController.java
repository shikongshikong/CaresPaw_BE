package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.videoCall.*;
import com.example.carespawbe.service.Expert.AppointmentService;
import com.example.carespawbe.service.Expert.videoCalling.ExpertCallingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expert/appointments")
public class ExpertCallingController {

    private final AppointmentService appointmentService;
    private final ExpertCallingService expertCallingService;

    public ExpertCallingController(AppointmentService appointmentService, ExpertCallingService expertCallingService) {
        this.appointmentService = appointmentService;
        this.expertCallingService = expertCallingService;
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
//    @PostMapping("/{id}/call/start")
//    public StartCallResponse startCall(
//            @PathVariable Long id,
//            @RequestParam Long expertId
//    ) {
//        return appointmentService.startCallAsExpert(id, expertId);
//    }

    // POST /api/expert/appointments/{id}/call/end
//    @PostMapping("/{id}/call/end")
//    public void endCall(
//            @PathVariable Long id,
//            @RequestParam Long expertId,
//            @RequestBody EndCallRequest body
//    ) {
//        appointmentService.endCallAsExpert(id, expertId, body.markSuccess());
//    }

    // for calling
    @GetMapping("/today")
    public ResponseEntity<?> getToday(HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(expertCallingService.getTodayCases(expertId));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<?> getDetail(@PathVariable Long appointmentId, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(expertCallingService.getDetail(expertId, appointmentId));
    }

    @PostMapping("/{appointmentId}/join")
    public ResponseEntity<?> join(@PathVariable Long appointmentId, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(expertCallingService.joinCall(expertId, appointmentId));
    }

    @PutMapping("/{appointmentId}/medical-record")
    public ResponseEntity<?> saveMedicalRecord(
            @PathVariable Long appointmentId,
            @RequestBody SaveMedicalRecordRequest req,
            HttpServletRequest request
    ) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.status(401).body("Unauthorized");
        return ResponseEntity.ok(expertCallingService.saveMedicalRecord(expertId, appointmentId, req));
    }

    @PostMapping("/{appointmentId}/end")
    public ResponseEntity<?> endCallAsExpert(
            @PathVariable("appointmentId") Long appointmentId,
            HttpServletRequest request
    ) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.status(401).build();

        appointmentService.endCall(appointmentId, null, expertId);
        return ResponseEntity.ok().build();
    }

}

