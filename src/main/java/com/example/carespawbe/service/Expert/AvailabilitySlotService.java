package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.booking.SlotForBookingResponse;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.repository.Expert.AvailabilitySlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {

    @Autowired
    private AvailabilitySlotRepository slotRepository;

    public AvailabilitySlotEntity saveAvailabilitySlot(AvailabilitySlotEntity availabilitySlotEntity) {
        return slotRepository.save(availabilitySlotEntity);
    }

    public List<SlotForBookingResponse> getSlotsForBooking(Long expertId, LocalDate date) {
        return slotRepository.findByExpert_IdAndDateAndBookedNotOrderByStartTimeAsc(expertId, date, 2)
                .stream()
                .map(s -> new SlotForBookingResponse(
                        s.getId(),
                        s.getStartTime(),
                        s.getEndTime(),
//                        new BigDecimal("100000"),
                        s.getPrice(),
                        s.getBooked()
                ))
                .toList();
    }

}
