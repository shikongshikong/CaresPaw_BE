package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.FeedbackRequest;
import com.example.carespawbe.dto.Shop.response.FeedbackResponse;
import com.example.carespawbe.entity.Shop.FeedbackEntity;
import com.example.carespawbe.service.Shop.FeedbackService;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
//@MultipartConfig
//@CrossOrigin(origins = "*")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping(path = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createFeedback(@ModelAttribute FeedbackRequest feedbackRequest, HttpServletRequest httpServletRequest) {
        Long userid = (Long) httpServletRequest.getAttribute("userId");
        feedbackRequest.setUserId(userid);
        try {
            FeedbackResponse feedback = feedbackService.createFeedback(feedbackRequest);
            return ResponseEntity.ok(feedback);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/product/{productId}")
    public List<FeedbackResponse> findByProductId(@PathVariable Long productId) {
        try{
            return feedbackService.findByProductId(productId);
        }catch (Exception e){
            return Collections.emptyList();
        }
    }

    @GetMapping(value = "/user/{userId}")
    public List<FeedbackResponse> findByUserId(@PathVariable Long userId) {
        try{
            return feedbackService.findByUserId(userId);
        }catch (Exception e){
            return Collections.emptyList();
        }
    }
}
