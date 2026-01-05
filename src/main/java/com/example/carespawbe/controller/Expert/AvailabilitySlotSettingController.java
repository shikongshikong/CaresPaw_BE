package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.SlotSettingRequest;
import com.example.carespawbe.service.Expert.AvailabilitySlotSettingCoordinator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expert")
@CrossOrigin(origins = "http://localhost:3000")
public class AvailabilitySlotSettingController {

    @Autowired
    private AvailabilitySlotSettingCoordinator slotCoordinator;

    @PostMapping("/slot-setting")
    public ResponseEntity<?> slotSetting(@RequestBody SlotSettingRequest slotSettingRequest, HttpServletRequest httpServletRequest) {
        Long expertId = Long.parseLong(httpServletRequest.getParameter("expertId"));

        boolean isCreateApp = slotCoordinator.createAppointment(expertId, slotSettingRequest);

        if (isCreateApp) return ResponseEntity.badRequest().build();
        else return ResponseEntity.ok().build();
    }

}
