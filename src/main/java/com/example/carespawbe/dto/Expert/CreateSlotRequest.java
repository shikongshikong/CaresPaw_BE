package com.example.carespawbe.dto.Expert;

import com.example.carespawbe.enums.ConflictStrategy;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class CreateSlotRequest {
    private LocalDate date;
    private String startTime;
    private Integer durationMin;
    private BigDecimal price;

    private RecurrencePayload recur;

    private ConflictStrategy strategy; // ASK/SKIP/OVERWRITE
    private Boolean dryRun;            // true => chỉ preview conflicts

    @Setter
    @Getter
    public static class RecurrencePayload {
        private String mode;              // none/daily/weekly/monthly
        private List<String> byWeekDays;  // weekly
        private LocalDate until;
    }
}
