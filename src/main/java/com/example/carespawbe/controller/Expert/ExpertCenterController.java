package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Common.PagedResponse;
import com.example.carespawbe.dto.Expert.ExpertAppListItem;
import com.example.carespawbe.dto.Expert.ExpertAppListRequest;
import com.example.carespawbe.dto.Expert.ExpertDashboardResponse;
import com.example.carespawbe.dto.Expert.ExpertEarningPageResponse;
import com.example.carespawbe.service.Expert.ExpertCenterCoordination;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/expert")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpertCenterController {

    @Autowired
    private ExpertCenterCoordination coordination;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getExpertDashboard(HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");

        if (expertId == null) {
            return ResponseEntity.badRequest().build();
        }

        ExpertDashboardResponse response = coordination.getDashBoardStatisticItem(expertId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/applist")
    public ResponseEntity<?> getExpertAppList(ExpertAppListRequest appListRequest, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");

        if (expertId == null) {
            return ResponseEntity.badRequest().build();
        }

        PagedResponse<ExpertAppListItem> appList = coordination.getAppList(expertId, appListRequest);
        return ResponseEntity.ok(appList);
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirm(@PathVariable("id") Long id, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        if (expertId == null) return ResponseEntity.badRequest().build();

        coordination.confirmApp(expertId, id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        coordination.cancelApp(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/earnings")
    public ExpertEarningPageResponse getMyEarnings(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String status, // all | 0 | 1
            @RequestParam(defaultValue = "1") int page,     // 1-based
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt,desc") String sort,
            HttpServletRequest request
    ) {
        Long expertId = (Long) request.getAttribute("expertId");
        Integer statusNum = parseStatus(status);

        String sortKey = "createAt";
        String sortDir = "desc";
        if (sort != null && sort.contains(",")) {
            String[] parts = sort.split(",");
            sortKey = parts[0].trim();
            sortDir = parts.length > 1 ? parts[1].trim() : "desc";
        }

//        return expertS.getExpertEarnings(from, to, statusNum, q, page, size, sortKey, sortDir);
        return coordination.getExpertEarnings(expertId, from, to, statusNum, q, page, size, sortKey, sortDir);
    }

    private Integer parseStatus(String status) {
        if (status == null || status.isBlank() || "all".equalsIgnoreCase(status)) return null;
        try { return Integer.parseInt(status.trim()); }
        catch (Exception ignored) { return null; }
    }
}
