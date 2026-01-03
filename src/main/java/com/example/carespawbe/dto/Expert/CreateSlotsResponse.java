package com.example.carespawbe.dto.Expert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateSlotsResponse {
    private int requestedOccurrences;
    private int createdCount;
    private int skippedCount;

    private List<Long> createdSlotIds;

    // conflicts to show in UI
    private List<SlotConflict> conflicts;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SlotConflict {
        private String date;        // yyyy-MM-dd
        private String startTime;   // HH:mm
        private String endTime;     // HH:mm
        private Long existingSlotId;
        private Integer existingBooked; // 0/1/2
        private boolean overwritable;   // booked != 1
        private String reason;          // "OVERLAP_WITH_EXISTING_SLOT" / "BOOKED_LOCKED" ...
    }
}

