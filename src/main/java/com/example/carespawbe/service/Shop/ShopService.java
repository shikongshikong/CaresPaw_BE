package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.ShopRequest;
import com.example.carespawbe.dto.Shop.response.ShopResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo, String token);
    ShopResponse updateShopInfo(Long userId, ShopRequest request, MultipartFile shopLogo);
    ShopResponse getShopByUserId(Long userId);
    ShopResponse getShopById(Long shopId);

}
