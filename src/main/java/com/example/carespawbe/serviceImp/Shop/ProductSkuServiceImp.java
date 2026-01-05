package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.SkuBulkUpdateRequest;
import com.example.carespawbe.dto.Shop.request.SkuGenerateRequest;
import com.example.carespawbe.dto.Shop.request.SkuPreviewRequest;
import com.example.carespawbe.dto.Shop.request.SkuUpdateRequest;
import com.example.carespawbe.dto.Shop.response.SkuResponse;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.service.Shop.ProductSkuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSkuServiceImp implements ProductSkuService {

    private final ProductRepository productRepository;
    private final VarriantValueRepository varriantValueRepository;

    private final ProductSkuRepository productSkuRepository;
    private final ProductSkuValueRepository productSkuValueRepository;

    // ===================== STEP: PREVIEW =====================
    @Override
    public List<SkuResponse> preview(Long productId, SkuPreviewRequest req) {
        if (req == null || req.getSelected() == null || req.getSelected().isEmpty()) {
            throw new RuntimeException("selected mapping is required (varriantId -> varriantValueIds)");
        }

        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int defaultStock = (req.getDefaultStock() != null) ? req.getDefaultStock() : 0;

        Map<Long, List<Long>> grouped = normalizeSelected(req.getSelected());
        List<List<Long>> combos = cartesianProduct(new ArrayList<>(grouped.values()));

        return combos.stream().map(valueIds -> SkuResponse.builder()
                .productSkuId(null)
                .skuCode(generateSkuCode(productId, valueIds))
                .skuName(buildAttributesTextByValueIds(valueIds))
                .stock(defaultStock)
                .price(null)
                .isActive(true)
                .varriantValueIds(valueIds)
                .build()
        ).toList();
    }

    // ===================== STEP: GENERATE (save SKU) =====================
    @Override
    @Transactional
    public List<SkuResponse> generate(Long productId, SkuGenerateRequest req) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        int defaultStock = (req != null && req.getDefaultStock() != null) ? req.getDefaultStock() : 0;
        BigDecimal defaultPrice = (req != null) ? req.getDefaultPrice() : null;

        List<SkuGenerateRequest.SkuItem> items = (req != null) ? req.getVariants() : null;

        if (items == null || items.isEmpty()) {
            throw new RuntimeException("variants list is required to generate SKUs when product_varriant is removed");
        }

        Set<String> existingComboKeys = loadExistingComboKeys(productId);

        List<SkuResponse> result = new ArrayList<>();

        for (SkuGenerateRequest.SkuItem item : items) {
            List<Long> valueIds = (item != null) ? item.getVarriantValueIds() : null;
            if (valueIds == null || valueIds.isEmpty()) continue;

            List<VarriantValueEntity> values = varriantValueRepository.findAllById(valueIds);
            if (values.size() != valueIds.size()) {
                throw new RuntimeException("Some varriantValueIds not found: " + valueIds);
            }

            validateOneValuePerVarriant(values);

            String comboKey = comboKey(values);
            if (existingComboKeys.contains(comboKey)) continue;
            existingComboKeys.add(comboKey);

            Integer stock = (item.getStock() != null) ? item.getStock() : defaultStock;
            BigDecimal price = (item.getPrice() != null) ? item.getPrice() : defaultPrice;

            String skuCode = makeUniqueSkuCode(generateSkuCode(productId, valueIds));

            ProductSkuEntity sku = ProductSkuEntity.builder()
                    .product(product)
                    .skuCode(skuCode)
                    .stock(stock == null ? 0 : stock)
                    .price(price)
                    .isActive(true)
                    .build();

            sku = productSkuRepository.save(sku);

            for (VarriantValueEntity vv : values) {
                VarriantEntity var = vv.getVarriant();

                ProductSkuValueEntity map = ProductSkuValueEntity.builder()
                        .id(ProductSkuValueId.builder()
                                .productSkuId(sku.getProductSkuId())
                                .varriantId(var.getVarriantId())
                                .build())
                        .productSku(sku)
                        .varriant(var)
                        .varriantValue(vv)
                        .build();

                productSkuValueRepository.save(map);
            }

            result.add(toResponse(sku));
        }

        // ✅ cực quan trọng: update amount sau generate
        recalcProductAmount(productId);

        return result;
    }

    // ===================== STEP: LIST =====================
    @Override
    public List<SkuResponse> listByProduct(Long productId) {
        // ✅ chỉ trả active để FE reload không hiện SKU đã delete
        return productSkuRepository.findByProduct_ProductIdAndIsActiveTrueOrderByProductSkuIdAsc(productId)
                .stream().map(this::toResponse).toList();
    }

    // ===================== STEP: UPDATE =====================
    @Override
    @Transactional
    public SkuResponse updateSku(Long skuId, SkuUpdateRequest req) {
        ProductSkuEntity sku = productSkuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuId));

        if (req.getStock() != null) sku.setStock(req.getStock());
        if (req.getPrice() != null) sku.setPrice(req.getPrice());
        if (req.getIsActive() != null) sku.setIsActive(req.getIsActive());

        ProductSkuEntity saved = productSkuRepository.save(sku);

        // ✅ update amount sau update sku
        recalcProductAmount(saved.getProduct().getProductId());

        return toResponse(saved);
    }

    // ===================== STEP: BULK UPDATE =====================
    @Override
    @Transactional
    public void bulkUpdate(Long productId, SkuBulkUpdateRequest req) {
        List<ProductSkuEntity> skus = productSkuRepository.findByProduct_ProductIdOrderByProductSkuIdAsc(productId);
        for (ProductSkuEntity sku : skus) {
            if (req.getStock() != null) sku.setStock(req.getStock());
            if (req.getPrice() != null) sku.setPrice(req.getPrice());
        }
        productSkuRepository.saveAll(skus);

        // ✅ update amount sau bulk
        recalcProductAmount(productId);
    }

    // ===================== STEP: DISABLE =====================
    @Override
    @Transactional
    public void disableSku(Long skuId) {
        ProductSkuEntity sku = productSkuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuId));

        sku.setIsActive(false);
        ProductSkuEntity saved = productSkuRepository.save(sku);

        // ✅ update amount sau disable
        recalcProductAmount(saved.getProduct().getProductId());
    }

    // ====== BỔ SUNG CHUẨN CHO CART/ORDER ======
    @Override
    public SkuResponse getById(Long skuId) {
        ProductSkuEntity sku = productSkuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuId));
        return toResponse(sku);
    }

    @Override
    public String buildVariantText(Long skuId) {
        ProductSkuEntity sku = productSkuRepository.findById(skuId)
                .orElseThrow(() -> new RuntimeException("SKU not found: " + skuId));

        List<ProductSkuValueEntity> maps =
                productSkuValueRepository.findByProductSku_ProductSkuId(sku.getProductSkuId());

        if (maps == null || maps.isEmpty()) return "";

        return maps.stream()
                .filter(m -> m.getVarriant() != null && m.getVarriantValue() != null)
                .sorted(Comparator.comparing(m -> m.getVarriant().getVarriantName()))
                .map(m -> m.getVarriant().getVarriantName() + ": " + m.getVarriantValue().getValueName())
                .collect(Collectors.joining(" | "));
    }

    // ===================== AMOUNT RECALC (NEW) =====================
    private void recalcProductAmount(Long productId) {
        Integer sum = productSkuRepository.sumActiveStockByProductId(productId);
        int amount = (sum == null ? 0 : sum);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        product.setProductAmount(amount); // ✅ không null
        productRepository.save(product);
    }

    // ===================== HELPERS =====================

    private Map<Long, List<Long>> normalizeSelected(Map<Long, List<Long>> selected) {
        Map<Long, List<Long>> grouped = new LinkedHashMap<>();
        selected.forEach((k, v) -> {
            if (k == null || v == null || v.isEmpty()) return;
            List<Long> clean = v.stream().filter(Objects::nonNull).distinct().toList();
            if (!clean.isEmpty()) grouped.put(k, clean);
        });
        if (grouped.isEmpty()) throw new RuntimeException("selected mapping is empty");
        return grouped;
    }

    private List<List<Long>> cartesianProduct(List<List<Long>> lists) {
        List<List<Long>> result = new ArrayList<>();
        result.add(new ArrayList<>());

        for (List<Long> list : lists) {
            List<List<Long>> newResult = new ArrayList<>();
            for (List<Long> prefix : result) {
                for (Long item : list) {
                    List<Long> next = new ArrayList<>(prefix);
                    next.add(item);
                    newResult.add(next);
                }
            }
            result = newResult;
        }
        return result;
    }

    private SkuResponse toResponse(ProductSkuEntity sku) {
        List<ProductSkuValueEntity> maps =
                productSkuValueRepository.findByProductSku_ProductSkuId(sku.getProductSkuId());

        List<VarriantValueEntity> values = maps.stream()
                .map(ProductSkuValueEntity::getVarriantValue)
                .toList();

        String attrs = buildAttributesText(values);
        List<Long> valueIds = values.stream()
                .map(VarriantValueEntity::getVarriantValueId)
                .toList();

        return SkuResponse.builder()
                .productSkuId(sku.getProductSkuId())
                .skuCode(sku.getSkuCode())
                .skuName(attrs)
                .stock(sku.getStock())
                .price(sku.getPrice())
                .isActive(sku.getIsActive())
                .varriantValueIds(valueIds)
                .build();
    }

    private String buildAttributesTextByValueIds(List<Long> valueIds) {
        List<VarriantValueEntity> values = varriantValueRepository.findAllById(valueIds);
        return buildAttributesText(values);
    }

    private String buildAttributesText(List<VarriantValueEntity> values) {
        return values.stream()
                .sorted(Comparator.comparing(v -> v.getVarriant().getVarriantName()))
                .map(v -> v.getVarriant().getVarriantName() + ": " + v.getValueName())
                .collect(Collectors.joining(" | "));
    }

    private void validateOneValuePerVarriant(List<VarriantValueEntity> values) {
        Set<Long> seen = new HashSet<>();
        for (VarriantValueEntity vv : values) {
            Long varId = vv.getVarriant().getVarriantId();
            if (!seen.add(varId)) {
                throw new RuntimeException("Invalid combo: duplicate varriant in one SKU");
            }
        }
    }

    private Set<String> loadExistingComboKeys(Long productId) {
        List<ProductSkuEntity> existingSkus = productSkuRepository.findByProduct_ProductIdOrderByProductSkuIdAsc(productId);
        Set<String> keys = new HashSet<>();

        for (ProductSkuEntity sku : existingSkus) {
            List<ProductSkuValueEntity> maps = productSkuValueRepository.findByProductSku_ProductSkuId(sku.getProductSkuId());
            List<VarriantValueEntity> vals = maps.stream().map(ProductSkuValueEntity::getVarriantValue).toList();
            if (!vals.isEmpty()) keys.add(comboKey(vals));
        }
        return keys;
    }

    private String comboKey(List<VarriantValueEntity> values) {
        return values.stream()
                .sorted(Comparator.comparing(v -> v.getVarriant().getVarriantId()))
                .map(v -> v.getVarriant().getVarriantId() + ":" + v.getVarriantValueId())
                .collect(Collectors.joining("|"));
    }

    private String generateSkuCode(Long productId, List<Long> valueIds) {
        List<VarriantValueEntity> values = varriantValueRepository.findAllById(valueIds);
        values.sort(Comparator.comparing(v -> v.getVarriant().getVarriantName()));

        String suffix = values.stream()
                .map(v -> slug(v.getValueName()))
                .collect(Collectors.joining("-"));

        return "PRD" + String.format("%06d", productId) + "-" + suffix;
    }

    private String makeUniqueSkuCode(String skuCode) {
        if (!productSkuRepository.existsBySkuCode(skuCode)) return skuCode;
        int i = 2;
        while (productSkuRepository.existsBySkuCode(skuCode + "-" + i)) i++;
        return skuCode + "-" + i;
    }

    private String slug(String input) {
        if (input == null) return "";
        String s = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        s = s.replaceAll("[^a-zA-Z0-9]+", "-").replaceAll("(^-|-$)", "");
        return s.toUpperCase();
    }
}
