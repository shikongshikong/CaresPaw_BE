package com.example.carespawbe.dto.Shop.response;

import com.example.carespawbe.dto.UserProfile.UserInfoResponse;
import com.example.carespawbe.entity.Shop.FeedbackMediaEntity;

import java.time.LocalDate;
import java.util.List;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private Long feedbackId;
    private OrderItemResponse orderItem;
    private UserInfoResponse user;
    private Long shopId;
    private List<FeedbackMediaEntity> feedbackMedia;
    private Integer star;
    private String content;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
