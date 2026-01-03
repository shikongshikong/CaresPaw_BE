package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.booking.BookingCheckoutRequest;
import com.example.carespawbe.dto.Expert.booking.BookingCheckoutResult;
import com.example.carespawbe.service.Expert.booking.BookingCheckoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/qanda/bookings")
@RequiredArgsConstructor
public class AppointmentBookingController {
    private final BookingCheckoutService bookingCheckoutService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/checkout", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> checkout(
            @RequestPart("metadata") String metadataJson,
            @RequestPart(value = "petImage", required = false) MultipartFile petImage, HttpServletRequest request
    ) throws Exception {

        Long userId = (Long) request.getAttribute("userId");

        BookingCheckoutRequest req = objectMapper.readValue(metadataJson, BookingCheckoutRequest.class);
        BookingCheckoutResult result = bookingCheckoutService.checkout(req, petImage, userId);

        return ResponseEntity.ok(Map.of(
                "appointmentId", result.getAppointmentId(),
                "paymentId", result.getPaymentId(),
                "paymentUrl", result.getPaymentUrl()
        ));
    }
}
