package com.example.carespawbe.controller.Expert;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentDetailResponse;
import com.example.carespawbe.dto.Expert.videoCall.AppointmentListItemResponse;
import com.example.carespawbe.dto.Expert.videoCall.JoinCallResponse;
import com.example.carespawbe.dto.Expert.videoCall.StartCallResponse;
import com.example.carespawbe.service.Expert.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
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
//    public Page<AppointmentListItemResponse> getMyAppointments(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "20") int size,
//            HttpServletRequest request
//    ) {
//        Long userId = request.getAttribute("userId") == null ? null : (Long) request.getAttribute("userId");
//        System.out.println("userId = " + userId);
//        return appointmentService.listForUser(userId, page, size);
//    }
    public List<AppointmentListItemResponse> getMyAppointments(
            @RequestParam(value = "type", required = false) String type,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        return appointmentService.getMyAppointments(userId, type);
    }

    // GET /api/user/appointments/{id}
//    @GetMapping("/{id}")
//    public AppointmentDetailResponse getMyAppointmentDetail(
//            @PathVariable("id") Long id,
//            HttpServletRequest request
//    ) {
//        Long userId = (Long) request.getAttribute("userId");
//        return appointmentQueryService.getMyAppointmentDetail(userId, id);
//    }
//    public AppointmentDetailResponse detail(
//            @PathVariable Long id,
//            HttpServletRequest request
//    ) {
//        Long userId = (Long) request.getAttribute("userId");
//        return appointmentService.getDetailForUser(id, userId);
//    }

    // POST /api/user/appointments/{id}/call/join
//    @PostMapping("/{id}/join")
//    public JoinCallResponse joinCall(
//            @PathVariable("id") Long id,
//            HttpServletRequest request
//    ) {
//        Long userId = (Long) request.getAttribute("userId");
//        return appointmentService.joinCall(userId, id);
//    }
//    public StartCallResponse joinCall(
//            @PathVariable Long id,
//            HttpServletRequest request
//    ) {
//        Long userId = (Long) request.getAttribute("userId");
//        return appointmentService.joinCallAsUser(id, userId);
//    }

    // POST /api/user/appointments/{id}/cancel
    @PostMapping("/{id}/cancel")
    public void cancel(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        appointmentService.cancelAsUser(id, userId);
    }
}

