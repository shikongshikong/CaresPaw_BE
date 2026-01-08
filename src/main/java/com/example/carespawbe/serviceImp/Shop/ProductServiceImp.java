package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.*;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.mapper.Shop.ProductMapper;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.Shop.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageProductRepository imageProductRepository;
    private final ProductSkuValueRepository productSkuValueRepository;
    private final JwtService jwtService;

    private Long getUserIdFromAuthHeader(String authorizationHeader) {
        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        if (token == null || token.isBlank()) throw new RuntimeException("Missing token");
        return jwtService.extractUserId(token);
    }

    private ShopEntity getMyShop(String authorizationHeader) {
        Long userId = getUserIdFromAuthHeader(authorizationHeader);
        return shopRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("User chưa có shop"));
    }

    @Override
    public ProductResponse createProduct(ProductRequest request, MultipartFile[] images, MultipartFile video, String authorizationHeader) {
        try {
            if (images != null && images.length > 9) throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");

            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            ShopEntity shop = getMyShop(authorizationHeader);

            ProductEntity productEntity = new ProductEntity();
            productEntity.setCategory(category);
            productEntity.setShop(shop);
            productEntity.setProductName(request.getProductName());
            productEntity.setProductPrice(request.getProductPrice());
            productEntity.setProductAmount(request.getProductAmount());
            productEntity.setProductStatus(request.getProductStatus());
            productEntity.setProductUsing(request.getProductUsing());
            productEntity.setProductCreatedAt(LocalDate.now());
            productEntity.setProductUpdatedAt(LocalDate.now());

            ProductEntity savedProduct = productRepository.save(productEntity);

            // upload images
            if (images != null && images.length > 0) {
                List<ImageProductEntity> imageEntities = new ArrayList<>();
                for (MultipartFile file : images) {
                    Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(file, "products/images");
                    ImageProductEntity imageEntity = new ImageProductEntity();
                    imageEntity.setImageProduct(savedProduct);
                    imageEntity.setImageProductUrl(result.get("url"));
                    imageEntity.setImagePublicId(result.get("public_id"));
                    imageEntity.setUploadedAt(LocalDateTime.now());
                    imageEntities.add(imageEntity);
                }
                imageProductRepository.saveAll(imageEntities);
                savedProduct.setImageProductList(imageEntities);
            }

            // upload video
            if (video != null && !video.isEmpty()) {
                Map<String, String> videoResult = cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");
                savedProduct.setProductVideoUrl(videoResult.get("url"));
                savedProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            return productMapper.toProductResponse(productRepository.save(savedProduct));

        } catch (Exception e) {
            throw new RuntimeException("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request, MultipartFile[] images, MultipartFile video, String authorizationHeader) {
        try {
            ProductEntity existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            ShopEntity shop = getMyShop(authorizationHeader);
            if (existingProduct.getShop() == null || !existingProduct.getShop().getShopId().equals(shop.getShopId())) {
                throw new RuntimeException("Bạn không có quyền sửa sản phẩm này");
            }

            existingProduct.setProductName(request.getProductName());
            existingProduct.setProductPrice(request.getProductPrice());
            existingProduct.setProductAmount(request.getProductAmount());
            existingProduct.setProductStatus(request.getProductStatus());
            existingProduct.setProductUsing(request.getProductUsing());
            existingProduct.setCategory(category);
            existingProduct.setProductUpdatedAt(LocalDate.now());

            // replace images (nếu upload mới)
            if (images != null && images.length > 0) {
                List<ImageProductEntity> oldImages = imageProductRepository.findByImageProduct(existingProduct);
                for (ImageProductEntity img : oldImages) {
                    if (img.getImagePublicId() != null) cloudinaryService.deleteImage(img.getImagePublicId());
                }
                imageProductRepository.deleteAll(oldImages);

                List<ImageProductEntity> imageEntities = new ArrayList<>();
                for (MultipartFile file : images) {
                    Map<String, String> uploadResult = cloudinaryService.uploadImageUrlAndPublicId(file, "products/images");
                    ImageProductEntity imageEntity = new ImageProductEntity();
                    imageEntity.setImageProduct(existingProduct);
                    imageEntity.setImageProductUrl(uploadResult.get("url"));
                    imageEntity.setImagePublicId(uploadResult.get("public_id"));
                    imageEntity.setUploadedAt(LocalDateTime.now());
                    imageEntities.add(imageEntity);
                }
                imageProductRepository.saveAll(imageEntities);
                existingProduct.setImageProductList(imageEntities);
            }

            // replace video (nếu upload mới)
            if (video != null && !video.isEmpty()) {
                // nếu muốn xóa video cũ bằng publicId thì bạn tự thêm cloudinaryService.deleteVideo(...)
                Map<String, String> videoResult = cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");
                existingProduct.setProductVideoUrl(videoResult.get("url"));
                existingProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            return productMapper.toProductResponse(productRepository.save(existingProduct));

        } catch (Exception e) {
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

@Override
public ProductDetailResponse getProductDetailById(Long productId) {
    ProductEntity p = productRepository.findWithSkus(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    // skuValues rows (đã fetch varriant + varriantValue)
    List<ProductSkuValueEntity> rows = productSkuValueRepository.findAllByProductIdFetchVariant(productId);

    // 1) Map skuId -> Set<valueId>
    Map<Long, Set<Long>> skuToValueIds = new HashMap<>();
    for (ProductSkuValueEntity r : rows) {
        Long skuId = r.getProductSku().getProductSkuId();
        Long valueId = r.getVarriantValue().getVarriantValueId();
        skuToValueIds.computeIfAbsent(skuId, k -> new LinkedHashSet<>()).add(valueId);
    }

    // 2) Map SKUs
    List<SkuResponse> skus = (p.getSkuList() == null ? List.<ProductSkuEntity>of() : p.getSkuList())
            .stream()
            .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
            .map(s -> SkuResponse.builder()
                    .productSkuId(s.getProductSkuId())
                    .skuCode(s.getSkuCode())
                    .skuName(s.getSkuName())
                    .stock(Optional.ofNullable(s.getStock()).orElse(0))
                    .price(Optional.ofNullable(s.getPrice()).orElse(BigDecimal.ZERO))
                    .isActive(s.getIsActive())
                    .varriantValueIds(new ArrayList<>(skuToValueIds.getOrDefault(s.getProductSkuId(), Set.of())))
                    .build())
            .toList();

    // 3) totalStock per valueId (để FE disable option nếu = 0)
    Map<Long, Integer> totalStockByValueId = new HashMap<>();
    for (SkuResponse s : skus) {
        int stock = Optional.ofNullable(s.getStock()).orElse(0);
        for (Long valueId : Optional.ofNullable(s.getVarriantValueIds()).orElse(List.of())) {
            if (valueId == null) continue;
            totalStockByValueId.put(valueId, totalStockByValueId.getOrDefault(valueId, 0) + stock);
        }
    }

    // 4) Build variantGroups từ rows
    // varriantId -> group
    Map<Long, VariantGroupResponse> groupMap = new LinkedHashMap<>();
    // để tránh add trùng value trong 1 group
    Map<Long, Set<Long>> groupValueSeen = new HashMap<>();

    for (ProductSkuValueEntity r : rows) {
        VarriantEntity v = r.getVarriant();
        VarriantValueEntity vv = r.getVarriantValue();
        if (v == null || vv == null) continue;
        if (Boolean.FALSE.equals(vv.getIsActive())) continue;

        Long varriantId = v.getVarriantId();
        Long valueId = vv.getVarriantValueId();

        groupMap.putIfAbsent(varriantId,
                VariantGroupResponse.builder()
                        .varriantId(varriantId)
                        .varriantName(v.getVarriantName())
                        .values(new ArrayList<>())
                        .build()
        );

        groupValueSeen.putIfAbsent(varriantId, new LinkedHashSet<>());
        if (groupValueSeen.get(varriantId).add(valueId)) {
            // ✅ có tồn kho để FE chặn chọn
            int totalStock = totalStockByValueId.getOrDefault(valueId, 0);

            groupMap.get(varriantId).getValues().add(
                    VariantGroupResponse.Value.builder()
                            .varriantValueId(valueId)
                            .valueName(vv.getValueName())
                            .totalStock(totalStock) // ✅ cần field này trong DTO
                            .build()
            );
        }
    }

    List<VariantGroupResponse> variantGroups = new ArrayList<>(groupMap.values());

    // 5) Images
    List<String> imageUrls = imageProductRepository.findByImageProduct_ProductId(productId)
            .stream()
            .map(ImageProductEntity::getImageProductUrl)
            .filter(Objects::nonNull)
            .toList();

    return ProductDetailResponse.builder()
            .productId(p.getProductId())
            .productName(p.getProductName())
            .productPrice(p.getProductPrice())
            .productAmount(p.getProductAmount())
            .productStatus(p.getProductStatus())
            .productUsing(p.getProductUsing())
            .categoryId(p.getCategory() != null ? p.getCategory().getCategoryId() : null)
            .categoryName(p.getCategory() != null ? p.getCategory().getCategoryName() : null)
            .shopId(p.getShop() != null ? p.getShop().getShopId() : null)
            .shopName(p.getShop() != null ? p.getShop().getShopName() : null)
            .imageUrls(imageUrls)
            .productVideoUrl(p.getProductVideoUrl())
            .productCreatedAt(p.getProductCreatedAt())
            .productUpdatedAt(p.getProductUpdatedAt())
            .sold(p.getSold())
            .rating(p.getRating())
            .variantGroups(variantGroups)
            .skus(skus)
            .build();
}


    private List<SkuResponse> mapSkuResponses(ProductEntity product) {
        if (product.getSkuList() == null) return List.of();

        return product.getSkuList().stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsActive()))
                .map(sku -> {
                    List<Long> valueIds = (sku.getSkuValues() == null) ? List.of()
                            : sku.getSkuValues().stream()
                            .map(x -> x.getVarriantValue() != null ? x.getVarriantValue().getVarriantValueId() : null)
                            .filter(Objects::nonNull)
                            .distinct()
                            .toList();

                    return SkuResponse.builder()
                            .productSkuId(sku.getProductSkuId())
                            .skuCode(sku.getSkuCode())
                            .skuName(sku.getSkuName())
                            .stock(Optional.ofNullable(sku.getStock()).orElse(0))
                            .price(Optional.ofNullable(sku.getPrice()).orElse(BigDecimal.ZERO))
                            .isActive(sku.getIsActive())
                            .varriantValueIds(valueIds) // ✅ not null
                            .build();
                })
                .toList();
    }

    private Map<Long, Integer> calcTotalStockByValueId(List<SkuResponse> skus) {
        Map<Long, Integer> m = new HashMap<>();
        for (SkuResponse s : skus) {
            int stock = Optional.ofNullable(s.getStock()).orElse(0);
            List<Long> ids = (s.getVarriantValueIds() == null) ? List.of() : s.getVarriantValueIds();
            for (Long id : ids) {
                if (id == null) continue;
                m.put(id, m.getOrDefault(id, 0) + stock);
            }
        }
        return m;
    }

    private List<VariantGroupResponse> buildVariantGroupsFromSkuValues(
            ProductEntity product,
            Map<Long, Integer> totalStockByValueId,
            List<SkuResponse> skus
    ) {
        if (product.getSkuList() == null) return List.of();

        // varriantId -> (varriantName, valueId->valueName)
        Map<Long, VariantGroupResponse> groupMap = new LinkedHashMap<>();
        Map<Long, String> valueNameMap = new HashMap<>(); // valueId -> name

        for (ProductSkuEntity sku : product.getSkuList()) {
            if (!Boolean.TRUE.equals(sku.getIsActive())) continue;
            if (sku.getSkuValues() == null) continue;

            for (ProductSkuValueEntity sv : sku.getSkuValues()) {
                VarriantEntity v = sv.getVarriant();
                VarriantValueEntity vv = sv.getVarriantValue();
                if (v == null || vv == null) continue;
                if (Boolean.FALSE.equals(vv.getIsActive())) continue;

                groupMap.putIfAbsent(
                        v.getVarriantId(),
                        VariantGroupResponse.builder()
                                .varriantId(v.getVarriantId())
                                .varriantName(v.getVarriantName())
                                .values(new ArrayList<>())
                                .build()
                );

                valueNameMap.put(vv.getVarriantValueId(), vv.getValueName());
            }
        }

        // build value list unique by group
        for (VariantGroupResponse g : groupMap.values()) {
            // collect valueIds belonging to this varriant from skuValues
            Set<Long> valueIds = new LinkedHashSet<>();
            for (ProductSkuEntity sku : product.getSkuList()) {
                if (!Boolean.TRUE.equals(sku.getIsActive())) continue;
                if (sku.getSkuValues() == null) continue;

                for (ProductSkuValueEntity sv : sku.getSkuValues()) {
                    if (sv.getVarriant() == null || sv.getVarriantValue() == null) continue;
                    if (!Objects.equals(sv.getVarriant().getVarriantId(), g.getVarriantId())) continue;
                    if (Boolean.FALSE.equals(sv.getVarriantValue().getIsActive())) continue;

                    valueIds.add(sv.getVarriantValue().getVarriantValueId());
                }
            }

            List<VariantGroupResponse.Value> values = new ArrayList<>();
            for (Long valueId : valueIds) {
                Integer totalStock = totalStockByValueId.getOrDefault(valueId, 0);

                // optional: min/max price của các SKU chứa value này (để FE show tham khảo)
                Double minP = null, maxP = null;
                for (SkuResponse s : skus) {
                    if (s.getVarriantValueIds() == null) continue;
                    if (!s.getVarriantValueIds().contains(valueId)) continue;

                    double price = s.getPrice() != null ? s.getPrice().doubleValue() : 0.0;
                    minP = (minP == null) ? price : Math.min(minP, price);
                    maxP = (maxP == null) ? price : Math.max(maxP, price);
                }

                values.add(VariantGroupResponse.Value.builder()
                        .varriantValueId(valueId)
                        .valueName(valueNameMap.getOrDefault(valueId, ""))
                        .totalStock(totalStock) // ✅ FE disable nếu 0
                        .minPrice(minP)
                        .maxPrice(maxP)
                        .build());
            }
            g.setValues(values);
        }

        return new ArrayList<>(groupMap.values());
    }

    @Override
    public void deleteProduct(Long productId, String authorizationHeader) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ShopEntity myShop = getMyShop(authorizationHeader);
        if (product.getShop() == null || !product.getShop().getShopId().equals(myShop.getShopId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm này");
        }

        // delete images + cloudinary files
        List<ImageProductEntity> oldImages = imageProductRepository.findByImageProduct(product);
        for (ImageProductEntity img : oldImages) {
            if (img.getImagePublicId() != null) cloudinaryService.deleteImage(img.getImagePublicId());
        }
        imageProductRepository.deleteAll(oldImages);

        // nếu có xóa video cloudinary thì bạn tự thêm bằng publicId
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getNewProducts() {
        return productRepository.findTop6ByOrderByProductCreatedAtDesc().stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsByShopId(Long shopId) {
        return productRepository.findAllByShop_ShopIdOrderByProductCreatedAtDesc(shopId).stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsByShopIdAndCategoryId(Long shopId, Long categoryId) {
        return productRepository
                .findAllByShop_ShopIdAndCategory_CategoryIdOrderByProductCreatedAtDesc(shopId, categoryId)
                .stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId).stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    private List<VarriantValueResponse> buildVarriantValues(ProductEntity product) {
        if (product.getSkuList() == null) return List.of();

        // distinct theo varriantId + varriantValueId
        java.util.Map<String, com.example.carespawbe.dto.Shop.response.VarriantValueResponse> map = new java.util.LinkedHashMap<>();

        for (ProductSkuEntity sku : product.getSkuList()) {
            if (sku.getSkuValues() == null) continue;

            for (ProductSkuValueEntity sv : sku.getSkuValues()) {
                VarriantEntity v = sv.getVarriant();
                VarriantValueEntity vv = sv.getVarriantValue();
                if (v == null || vv == null) continue;

                Long varriantId = v.getVarriantId();
                Long varriantValueId = vv.getVarriantValueId();
                if (varriantId == null || varriantValueId == null) continue;

                String key = varriantId + ":" + varriantValueId;

                map.putIfAbsent(key,
                        com.example.carespawbe.dto.Shop.response.VarriantValueResponse.builder()
                                .varriantValueId(varriantValueId)
                                .varriantId(varriantId)
                                .varriantName(v.getVarriantName())
                                .valueName(vv.getValueName())
                                .isActive(vv.getIsActive() == null ? true : vv.getIsActive())
                                .build()
                );
            }
        }

        // nếu muốn chỉ trả active values:
        return map.values().stream()
                .filter(x -> x.getIsActive() == null || Boolean.TRUE.equals(x.getIsActive()))
                .toList();
    }

    // =========================
    // ✅ ADD: Best Seller Products
    // =========================
    @Override
    public List<ProductResponse> getBestSellers() {
        return productRepository.findTop12ByOrderBySoldDesc()
                .stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductInfoDTO> getAllProductInfos() {
        return productRepository.findAllProductInfos();
    }

}
