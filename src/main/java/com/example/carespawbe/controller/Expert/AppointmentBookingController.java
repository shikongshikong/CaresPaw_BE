package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.booking.BookingCheckoutRequest;
import com.example.carespawbe.dto.Expert.booking.BookingCheckoutResult;
import com.example.carespawbe.dto.Expert.booking.PaymentInfo;
import com.example.carespawbe.dto.Expert.booking.SlotForBookingResponse;
import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.repository.Expert.AppointmentRepository;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import com.example.carespawbe.service.Expert.AvailabilitySlotService;
import com.example.carespawbe.service.Expert.booking.BookingCheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qanda/bookings")
@RequiredArgsConstructor
public class AppointmentBookingController {
    private final BookingCheckoutService bookingCheckoutService;
    private final ObjectMapper objectMapper;
    private final AvailabilitySlotService slotService;
    private final AppointmentRepository  appointmentRepository;
    private final ExpertRepository expertRepository;

    @GetMapping("/{expertId}/slots")
    public List<SlotForBookingResponse> getSlots(
            @PathVariable Long expertId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return slotService.getSlotsForBooking(expertId, date);
    }

    @PostMapping(value = "/checkout", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> checkout(
            @RequestPart("metadata") String metadataJson,
            @RequestPart(value = "petImage", required = false) MultipartFile petImage,
            HttpServletRequest request
    ) throws Exception {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) throw new RuntimeException("Unauthenticated");

        BookingCheckoutRequest req = objectMapper.readValue(metadataJson, BookingCheckoutRequest.class);
        PaymentInfo info =  objectMapper.readValue(metadataJson, PaymentInfo.class);
        info.setMethod("cash");
        req.setPayment(info);
        BookingCheckoutResult result = bookingCheckoutService.checkout(req, petImage, userId);

//        return ResponseEntity.ok(Map.of(
//                "appointmentId", result.getAppointmentId(),
//                "paymentId", result.getPaymentId(),
//                "paymentUrl", result.getPaymentUrl()
//        ));
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/{appointmentId}/cancel")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long appointmentId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) throw new RuntimeException("Unauthenticated");

        AppointmentEntity app = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        // (tuỳ chọn) check ownership – bỏ cũng được
        if (app.getUser() != null && app.getUser().getId() != null
                && !app.getUser().getId().equals(userId)) {
            throw new RuntimeException("Forbidden");
        }
        app.setStatus(3); // 3 = Cancelled

        // ✅ trả slot lại available
        if (app.getSlot() != null) {
            AvailabilitySlotEntity slot = app.getSlot();
            slot.setBooked(0); // 0 = available
        }

        appointmentRepository.save(app);

        return ResponseEntity.ok(
                Map.of("message", "Appointment cancelled successfully")
        );
    }
    @GetMapping("/{expertId}/can-book")
    public ResponseEntity<?> canBookExpert(
            @PathVariable Long expertId,
            HttpServletRequest request
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) userId = (Long) request.getSession().getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(
                    Map.of("allowed", false, "message", "Unauthenticated")
            );
        }

        ExpertEntity expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new RuntimeException("Expert not found"));

        if (expert.getUser() != null
                && expert.getUser().getId() != null
                && expert.getUser().getId().equals(userId)) {
            return ResponseEntity.ok(
                    Map.of(
                            "allowed", false,
                            "message", "You cannot book an appointment with yourself."
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of("allowed", true)
        );
    }
}
