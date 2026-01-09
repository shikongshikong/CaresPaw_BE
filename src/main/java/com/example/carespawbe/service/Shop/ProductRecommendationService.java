package com.example.carespawbe.service.Shop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class ProductRecommendationService {

    @Autowired
    private WebClient webClient;

    // Bạn có thể đưa baseUrl vào application.properties sau (mục 6)
    private final String aiUrl = "http://localhost:5001/getProductRecommender/";

    public List<Long> getRecommendedProducts(Long userId) {

        Map<String, Object> body = Map.of("userId", userId);

        return webClient.post()
                .uri(aiUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(Long.class)
                .collectList()
                .block(); // blocking vì bạn đang dùng MVC
    }
}
