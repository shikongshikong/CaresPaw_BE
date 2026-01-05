package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantValueRequest;
import com.example.carespawbe.dto.Shop.response.VarriantValueResponse;

import java.util.List;

public interface VarriantValueService {

    // giữ cũ
    List<VarriantValueResponse> getValuesByVarriantId(Long varriantId);
    VarriantValueResponse create(VarriantValueRequest request);
    VarriantValueResponse update(Long id, VarriantValueRequest request);
    void delete(Long id);

    // bổ sung
    VarriantValueResponse getById(Long id);
    void toggleActive(Long id, Boolean isActive);
}
