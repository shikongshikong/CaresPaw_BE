package com.example.carespawbe.controller.Expert;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/expert")
//@CrossOrigin(origins = "http://localhost:3000")
//public class ExpertCenterController {
//
//    @GetMapping("/dashboard")
//    public ResponseEntity<?> getExpertDashboard(HttpServletRequest request) {
//        Long expertId = (Long) request.getSession().getAttribute("expertId");
//
//        if (expertId == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//
//    }
//}
