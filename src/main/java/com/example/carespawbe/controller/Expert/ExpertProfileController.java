package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.ExpertCVResponse;
import com.example.carespawbe.dto.Expert.ExpertCVUpdateRequest;
import com.example.carespawbe.service.Expert.ExpertEntityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expert")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpertProfileController {

    @Autowired
    private ExpertEntityService expertService;

    @GetMapping("/cv")
    public ResponseEntity<ExpertCVResponse> getExpertCV(HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        return ResponseEntity.ok(expertService.getExpertCv(expertId));
    }

    @PutMapping("/cv")
    public ResponseEntity<ExpertCVResponse> updateMyCv(@RequestBody ExpertCVUpdateRequest req, HttpServletRequest request) {
        Long expertId = (Long) request.getAttribute("expertId");
        return ResponseEntity.ok(expertService.updateExpertCv(expertId, req));
    }

}
