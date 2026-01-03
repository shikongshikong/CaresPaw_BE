package com.example.carespawbe.service.Expert;

import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.repository.Expert.AvailabilitySlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotService {

    @Autowired
    private AvailabilitySlotRepository availabilitySlotRepository;

    public AvailabilitySlotEntity saveAvailabilitySlot(AvailabilitySlotEntity availabilitySlotEntity) {
        return availabilitySlotRepository.save(availabilitySlotEntity);
    }


}
