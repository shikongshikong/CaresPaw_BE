package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.FeedbackMediaRequest;
import com.example.carespawbe.dto.Shop.request.FeedbackRequest;
import com.example.carespawbe.dto.Shop.response.FeedbackResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.mapper.Shop.FeedbackMapper;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.Shop.FeedbackService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class FeedbackServiceImp implements FeedbackService {
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackResponse createFeedback(FeedbackRequest request) {
        FeedbackEntity feedback = new FeedbackEntity();

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        ShopEntity shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found!"));

        OrderItemEntity orderItem = orderItemRepository.findById(request.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("Order item not found!"));

        feedback.setUser(user);
        feedback.setShop(shop);
        feedback.setOrderItem(orderItem);
        feedback.setStar(request.getStar());
        feedback.setContent(request.getContent());

        List<FeedbackMediaEntity> feedbackMedias = new ArrayList<>();
        if (request.getFeedbackMedia() != null && !request.getFeedbackMedia().isEmpty()) {
            for (FeedbackMediaRequest feedbackMediaRequest : request.getFeedbackMedia()) {

                FeedbackMediaEntity feedbackMedia = new FeedbackMediaEntity();
                String type = feedbackMediaRequest.getResourceType();
                feedbackMedia.setResourceType(type);
                feedbackMedia.setFeedback(feedback);

                if ("IMAGE".equalsIgnoreCase(type)) {
                    Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(
                            feedbackMediaRequest.getFile(), "feedbacks/images");
                    feedbackMedia.setPublicId(result.get("public_id"));
                    feedbackMedia.setSecureUrl(result.get("url"));
                    feedbackMedias.add(feedbackMedia);

                } else if ("VIDEO".equalsIgnoreCase(type)) {
                    Map<String, String> result = cloudinaryService.uploadVideoUrlAndPublicId(
                            feedbackMediaRequest.getFile(), "feedbacks/videos");
                    feedbackMedia.setPublicId(result.get("public_id"));
                    feedbackMedia.setSecureUrl(result.get("url"));
                    feedbackMedias.add(feedbackMedia);
                }
            }
        }
        feedback.setFeedbackMedia(feedbackMedias);

        // Lưu feedback trước
        FeedbackEntity savedFeedback = feedbackRepository.save(feedback);

        // ✅ Lấy productId theo flow SKU mới: OrderItem -> ProductSku -> Product
        if (orderItem.getProductSku() == null || orderItem.getProductSku().getProduct() == null) {
            throw new RuntimeException("OrderItem does not have ProductSku/Product (invalid data)!");
        }

        Long productId = orderItem.getProductSku().getProduct().getProductId();

        // 1) Tính rating trung bình mới
        Double newRating = feedbackRepository.getAverageRatingByProductId(productId);

        // 2) null + làm tròn 1 chữ số
        if (newRating == null) newRating = 0.0;
        newRating = Math.round(newRating * 10.0) / 10.0;

        // 3) Update Product.rating
        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product != null) {
            product.setRating(newRating);
            productRepository.save(product);
        }

        return feedbackMapper.toResponse(savedFeedback);
    }

    @Override
    public List<FeedbackResponse> findByProductId(Long productId) {
        List<FeedbackEntity> feedback = feedbackRepository.findAllByOrderItem_ProductSku_Product_ProductId(productId);
        return feedbackMapper.toResponseList(feedback);
    }

    @Override
    public List<FeedbackResponse> findByUserId(Long userId) {
        List<FeedbackEntity> feedback = feedbackRepository.findAllByUser_Id(userId);
        return feedbackMapper.toResponseList(feedback);
    }
}