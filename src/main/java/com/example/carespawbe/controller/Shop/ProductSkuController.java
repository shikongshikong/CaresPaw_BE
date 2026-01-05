package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.SkuBulkUpdateRequest;
import com.example.carespawbe.dto.Shop.request.SkuGenerateRequest;
import com.example.carespawbe.dto.Shop.request.SkuPreviewRequest;
import com.example.carespawbe.dto.Shop.request.SkuUpdateRequest;
import com.example.carespawbe.dto.Shop.response.SkuResponse;
import com.example.carespawbe.service.Shop.ProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product-sku")
@CrossOrigin(origins = "*")
public class ProductSkuController {

    private final ProductSkuService productSkuService;

    @PostMapping("/{productId}/skus/preview")
    public List<SkuResponse> preview(
            @PathVariable Long productId,
            @RequestBody SkuPreviewRequest req
    ) {
        return productSkuService.preview(productId, req);
    }

    @PostMapping("/{productId}/skus/generate")
    public List<SkuResponse> generate(
            @PathVariable Long productId,
            @RequestBody SkuGenerateRequest req
    ) {
        return productSkuService.generate(productId, req);
    }

    @GetMapping("/{productId}/skus")
    public List<SkuResponse> list(@PathVariable Long productId) {
        return productSkuService.listByProduct(productId);
    }

    @PutMapping("/skus/{skuId}")
    public SkuResponse update(
            @PathVariable Long skuId,
            @RequestBody SkuUpdateRequest req
    ) {
        return productSkuService.updateSku(skuId, req);
    }

    @PutMapping("/{productId}/skus/bulk")
    public void bulk(
            @PathVariable Long productId,
            @RequestBody SkuBulkUpdateRequest req
    ) {
        productSkuService.bulkUpdate(productId, req);
    }

    @DeleteMapping("/skus/{skuId}")
    public void disable(@PathVariable Long skuId) {
        productSkuService.disableSku(skuId);
    }
}
