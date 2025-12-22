package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantRequest;
import com.example.carespawbe.dto.Shop.response.VarriantResponse;

import java.util.List;

public interface VarriantService {
    List<VarriantResponse> getAllVarriants();

    VarriantResponse createVarriant(VarriantRequest request);
    VarriantResponse updateVarriant(Long id, VarriantRequest request);
    void deleteVarriant(Long id);
}
