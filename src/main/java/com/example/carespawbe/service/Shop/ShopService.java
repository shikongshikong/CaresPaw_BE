package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.ShopRequest;
import com.example.carespawbe.dto.Shop.response.ShopResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo,MultipartFile shopBanner, String token);
    ShopResponse updateShopInfo(Long userId, ShopRequest request, MultipartFile shopLogo, MultipartFile shopBanner);
    ShopResponse getShopByUserId(Long userId);
    ShopResponse getShopById(Long shopId);

    Page<ShopResponse> getAllShopsForAdmin(int page, int size);
    void lockShop(Long shopId);
    void unlockShop(Long shopId);
}
