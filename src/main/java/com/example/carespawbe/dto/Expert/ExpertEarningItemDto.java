package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
public class ExpertEarningItemDto {
    private Long id;
    private BigDecimal totalEarning;
    private BigDecimal platformFee;
    private BigDecimal expertGain;
    private Integer status;
    private LocalDateTime createAt;

    private AppointmentBriefDto appointmentEntity;

    public ExpertEarningItemDto(Long id, Long appointmentId, BigDecimal totalEarning, BigDecimal platformFee,
                                BigDecimal expertGain, Integer status, LocalDateTime createAt) {
        this.id = id;
        this.totalEarning = totalEarning;
        this.platformFee = platformFee;
        this.expertGain = expertGain;
        this.status = status;
        this.createAt = createAt;
        this.appointmentEntity = new AppointmentBriefDto(appointmentId);
    }

    @Setter
    @Getter
    public static class AppointmentBriefDto {
        private Long id;
        public AppointmentBriefDto() {}
        public AppointmentBriefDto(Long id) { this.id = id; }

    }

}

