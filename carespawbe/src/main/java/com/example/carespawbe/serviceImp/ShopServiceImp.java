package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.ShopRequest;
import com.example.carespawbe.dto.response.ShopResponse;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.entity.shop.ShopEntity;
import com.example.carespawbe.mapper.ShopMapper;
import com.example.carespawbe.repository.UserRepository;
import com.example.carespawbe.repository.shop.ShopRepository;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements ShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CloudinaryService cloudinaryService;
    private final ShopMapper shopMapper;

    @Override
    public ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (shopRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new RuntimeException("User has already registered a shop");
        }

        user.setPhoneNumber(request.getShopPhone());
        user.setRole(2);
        userRepository.save(user);

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopName(request.getShopName());
        shopEntity.setShopAddress(request.getShopAddress());
        shopEntity.setUser(user);
        shopEntity.setCreated_at(LocalDate.now());
        shopEntity.setShopAmountFollower(0);

        if (shopLogo != null && !shopLogo.isEmpty()) {
            Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(shopLogo, "shops");
            shopEntity.setShopLogo(result.get("url"));
            shopEntity.setShopLogoPublicId(result.get("public_id"));
        } else {
            shopEntity.setShopLogo("");
            shopEntity.setShopLogoPublicId(""); // hoặc null nếu database chấp nhận
        }

        return shopMapper.toResponse(shopRepository.save(shopEntity));
    }

    @Override
    public ShopResponse updateShopInfo(Long userId, ShopRequest request, MultipartFile newShopLogo) {
        ShopEntity existingShop = shopRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        existingShop.setShopName(request.getShopName());
        existingShop.setShopAddress(request.getShopAddress());
        existingShop.setUpdate_at(LocalDate.now());

        if (newShopLogo != null && !newShopLogo.isEmpty()) {
            // Xóa logo cũ
            if (existingShop.getShopLogoPublicId() != null) {
                cloudinaryService.deleteImage(existingShop.getShopLogoPublicId());
            }

            Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(newShopLogo, "shops");
            existingShop.setShopLogo(result.get("url"));
            existingShop.setShopLogoPublicId(result.get("public_id"));
        }

        return shopMapper.toResponse(shopRepository.save(existingShop));
    }

    @Override
    public ShopResponse getShopByUserId(Long userId) {
        ShopEntity shop = shopRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found for user ID: " + userId));

        return shopMapper.toResponse(shop);
    }

}
