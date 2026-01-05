package com.example.carespawbe.dto.Expert;

import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Expert.AvailabilitySlotEntity;
import com.example.carespawbe.entity.Expert.PetEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;

@Setter
@Builder
@Getter
public class ExpertAppListItem {
    private Long appId;
    private String petImageUrl;
    private String petName;
    private String userName;
    private String date;
    private String startTime;
    private long duration;
    private int status;

    public static ExpertAppListItem fromAppEntity(AppointmentEntity app) {
        PetEntity pet = app.getPet();
        AvailabilitySlotEntity slot = app.getSlot();
        long minutes = Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes();
        return ExpertAppListItem.builder()
                .appId(app.getId())
                .petImageUrl(pet.getImageUrl())
                .petName(pet.getName())
                .userName(pet.getUser().getFullname())
                .date(slot.getDate().toString())
                .startTime(slot.getStartTime().toString())
                .duration(minutes)
                .status(app.getStatus())
                .build();
    }
}
