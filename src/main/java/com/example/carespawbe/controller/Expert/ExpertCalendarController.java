package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.CalendarItemResponse;
import com.example.carespawbe.dto.Expert.CreateSlotRequest;
import com.example.carespawbe.dto.Expert.CreateSlotsResponse;
import com.example.carespawbe.service.Expert.ExpertCalendarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/expert/me")
@RequiredArgsConstructor
public class ExpertCalendarController {

    private final ExpertCalendarService calendarService;

    @GetMapping("/calendar")
    public List<CalendarItemResponse> getMonthCalendar(
            @RequestParam("month") String month, HttpServletRequest request
    ) {
        Long expertId = (Long) request.getSession().getAttribute("expertId");
        YearMonth ym = YearMonth.parse(month);
        return calendarService.getMonthCalendar(expertId, ym);
    }

    @PostMapping("/slots")
    public CreateSlotsResponse createSlots(
            @RequestBody CreateSlotRequest req, HttpServletRequest request
    ) {
        Long expertId = (Long) request.getSession().getAttribute("expertId");
        return calendarService.createSlotsWithRecurrence(expertId, req);
    }
}



