package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.booking.BookingCheckoutRequest;
import com.example.carespawbe.dto.Expert.booking.BookingCheckoutResult;
import com.example.carespawbe.dto.Expert.booking.PaymentInfo;
import com.example.carespawbe.dto.Expert.booking.SlotForBookingResponse;
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

//    @PostMapping(value = "/checkout", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Map<String, Object>> checkout(
//            @RequestPart("metadata") String metadataJson,
//            @RequestPart(value = "petImage", required = false) MultipartFile petImage, HttpServletRequest request
//    ) throws Exception {
//
//        Long userId = (Long) request.getAttribute("userId");
//
//        BookingCheckoutRequest req = objectMapper.readValue(metadataJson, BookingCheckoutRequest.class);
//        BookingCheckoutResult result = bookingCheckoutService.checkout(req, petImage, userId);
//
//        return ResponseEntity.ok(Map.of(
//                "appointmentId", result.getAppointmentId(),
//                "paymentId", result.getPaymentId(),
//                "paymentUrl", result.getPaymentUrl()
//        ));
//    }

}
