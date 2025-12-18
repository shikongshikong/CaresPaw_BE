package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.ShopRequest;
import com.example.carespawbe.dto.Shop.response.ShopResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.ShopEntity;
import com.example.carespawbe.mapper.Shop.ShopMapper;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.ShopRepository;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.Shop.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements ShopService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final CloudinaryService cloudinaryService;
    private final ShopMapper shopMapper;
    private final JwtService jwtService;

    @Override
    public ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo,MultipartFile shopBanner, String token) {
        // Lấy token JWT (bỏ "Bearer ")
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Lấy userId từ token
        Long userId = jwtService.extractUserId(token);

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (shopRepository.findByUser_Id(request.getUserId()).isPresent()) {
            throw new RuntimeException("User has already registered a shop");
        }

        // Kiểm tra role hiện tại
        if (userEntity.getRole() != 1) { // ví dụ 1 = USER
            throw new RuntimeException("User not allowed to register shop");
        }

        userEntity.setPhoneNumber(request.getShopPhone());
        userEntity.setRole(2);
        userRepository.save(userEntity);

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopName(request.getShopName());
        shopEntity.setShopAddress(request.getShopAddress());
        shopEntity.setShopDescription(request.getShopDescription());
        shopEntity.setUser(userEntity);
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

        if (shopBanner != null && !shopBanner.isEmpty()) {
            Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(shopBanner, "shops");
            shopEntity.setShopBanner(result.get("url"));
            shopEntity.setShopBannerPublicId(result.get("public_id"));
        } else {
            shopEntity.setShopBanner("");
            shopEntity.setShopBannerPublicId("");
        }

        return shopMapper.toResponse(shopRepository.save(shopEntity));
    }

    @Override
    public ShopResponse updateShopInfo(Long userId, ShopRequest request, MultipartFile newShopLogo, MultipartFile newShopBanner) {
        ShopEntity existingShop = shopRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        existingShop.setShopName(request.getShopName());
        existingShop.setShopAddress(request.getShopAddress());
        existingShop.setShopDescription(request.getShopDescription());
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

        if (newShopBanner != null && !newShopBanner.isEmpty()) {
            if (existingShop.getShopBannerPublicId() != null && !existingShop.getShopBannerPublicId().isBlank()) {
                cloudinaryService.deleteImage(existingShop.getShopBannerPublicId());
            }

            Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(newShopBanner, "shops");
            existingShop.setShopBanner(result.get("url"));
            existingShop.setShopBannerPublicId(result.get("public_id"));
        }

        //update status (1=active, 2=block)
        if (request.getStatus() == 1 || request.getStatus() == 2) {
            existingShop.setStatus(request.getStatus());
        }

        return shopMapper.toResponse(shopRepository.save(existingShop));
    }

    @Override
    public ShopResponse getShopByUserId(Long userId) {
        ShopEntity shop = shopRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Shop not found for user ID: " + userId));

        return shopMapper.toResponse(shop);
    }

    @Override
    public ShopResponse getShopById(Long shopId) {
        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Shop not found with id " + shopId));

        return shopMapper.toResponse(shop);
    }

}
