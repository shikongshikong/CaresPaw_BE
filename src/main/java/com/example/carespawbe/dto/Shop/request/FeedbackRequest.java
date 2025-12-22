package com.example.carespawbe.dto.Shop.request;

import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackRequest {
    private Long orderItemId;
    private Long userId;
    private Long shopId;
    private List<FeedbackMediaRequest> feedbackMedia;
    private Integer star;
    private String content;
}
