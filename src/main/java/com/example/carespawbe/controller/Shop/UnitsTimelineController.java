package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.UnitsTimelineResponse;
import com.example.carespawbe.service.Shop.UnitsTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/units-timeline")
@CrossOrigin(origins = "*")
public class UnitsTimelineController {

    private final UnitsTimelineService unitsTimelineService;

    @GetMapping
    public List<UnitsTimelineResponse> getUnitsTimeline(
            @RequestParam Long shopId,
            @RequestParam(defaultValue = "5") int monthsBack,
            @RequestParam(defaultValue = "0") int monthsForward
    ) {
        return unitsTimelineService.getUnitsTimeline(shopId, monthsBack, monthsForward);
    }
}
