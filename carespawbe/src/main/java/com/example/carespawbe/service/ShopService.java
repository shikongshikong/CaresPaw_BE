package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.ShopRequest;
import com.example.carespawbe.dto.response.ShopResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ShopService {
    ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo);
    ShopResponse updateShopInfo(Long userId, ShopRequest request, MultipartFile shopLogo);
    ShopResponse getShopByUserId(Long userId);

}
