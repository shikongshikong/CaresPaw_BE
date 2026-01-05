package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.qanda.ExpertCardResponse;
import com.example.carespawbe.dto.Expert.qanda.ExpertProfileResponse;
import com.example.carespawbe.service.Expert.ExpertEntityService;
import com.example.carespawbe.service.Expert.qanda.QandAService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/qanda")
@RequiredArgsConstructor
public class QandAController {

    @Autowired
    private QandAService qandAService;

    @Autowired
    private ExpertEntityService expertService;

    // 1) Booked experts - user phải đăng nhập
    @GetMapping("/booked-experts")
    public List<ExpertCardResponse> getBookedExperts(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return qandAService.getBookedExperts(userId);
    }

    // 2) Public experts list with filters
    @GetMapping("/experts")
    public Page<ExpertCardResponse> getExperts(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return qandAService.searchExperts(q, minPrice, maxPrice, sort, page, size);
    }

    @GetMapping("/{expertId}/profile")
    public ExpertProfileResponse getExpertProfile(@PathVariable Long expertId) {
        return expertService.getProfile(expertId);
    }
}

