package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.FeedbackRequest;
import com.example.carespawbe.dto.Shop.response.FeedbackResponse;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse createFeedback (FeedbackRequest request);
    List<FeedbackResponse> findByProductId(Long productId);
    List<FeedbackResponse> findByUserId(Long userId);
}
