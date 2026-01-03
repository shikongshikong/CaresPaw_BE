package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.SlotSettingRequest;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

@Service
public class AvailabilitySlotSettingCoordinator {

    @Autowired private ExpertEntityService expertEntityService;
    @Autowired private AvailabilitySlotService availabilitySlotService;

    public boolean createAppointment(Long expertId, SlotSettingRequest slotSettingRequest){
        ExpertEntity expert = expertEntityService.getExpertById(expertId);
        if (expert == null) return false;

        LocalTime startTime = slotSettingRequest.getStartTime();
        int duration = slotSettingRequest.getDuration();
        LocalTime endTime = startTime.plusMinutes(duration);
        int isBooked = 0;
//        BigDecimal price = expert.getSessionPrice() * (duration / 15);
        BigDecimal durationBD = BigDecimal.valueOf(duration);
        BigDecimal divisor = BigDecimal.valueOf(15);
        BigDecimal price = expert.getSessionPrice()
                .multiply(durationBD)
                .divide(divisor, 2, RoundingMode.HALF_UP);

        AvailabilitySlotEntity availabilitySlotEntity = availabilitySlotService.saveAvailabilitySlot(
                new AvailabilitySlotEntity(slotSettingRequest.getDate(), startTime, endTime, price, isBooked, expert));

        if (availabilitySlotEntity == null) return false;

        // need create recurring

        return true;
    }
}
