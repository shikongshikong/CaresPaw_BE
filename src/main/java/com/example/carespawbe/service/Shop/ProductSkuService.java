package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.SkuBulkUpdateRequest;
import com.example.carespawbe.dto.Shop.request.SkuGenerateRequest;
import com.example.carespawbe.dto.Shop.request.SkuPreviewRequest;
import com.example.carespawbe.dto.Shop.request.SkuUpdateRequest;
import com.example.carespawbe.dto.Shop.response.SkuResponse;

import java.util.List;

public interface ProductSkuService {

    List<SkuResponse> preview(Long productId, SkuPreviewRequest req);
    List<SkuResponse> generate(Long productId, SkuGenerateRequest req);

    List<SkuResponse> listByProduct(Long productId);
    SkuResponse updateSku(Long skuId, SkuUpdateRequest req);

    void bulkUpdate(Long productId, SkuBulkUpdateRequest req);
    void disableSku(Long skuId);

    // ====== BỔ SUNG CHUẨN CHO CART/ORDER ======
    SkuResponse getById(Long skuId);
    String buildVariantText(Long skuId); // từ product_sku_value -> varriant/varriant_value
}
