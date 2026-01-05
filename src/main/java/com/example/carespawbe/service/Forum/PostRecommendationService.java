package com.example.carespawbe.service.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
public class PostRecommendationService {

    @Autowired
    private WebClient webClient;

    public List<Long> getRecommendedPosts(Long userId) {

        Map<String, Object> body = Map.of(
                "userId", userId
        );

        return webClient.post()
                .uri("http://localhost:5000/getPostRecommender/")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(Long.class)
                .collectList()
                .block();   // blocking vì bạn đang dùng MVC
    }

    public List<Long> getSimilarPosts(Long postId) {

        Map<String, Object> body = Map.of(
                "postId", postId
        );

        return webClient.post()
                .uri("http://localhost:5001/getPostSimilar/")
                .bodyValue(body)
                .retrieve()
                .bodyToFlux(Long.class)
                .collectList()
                .block();   // blocking vì bạn đang dùng MVC
    }
}
