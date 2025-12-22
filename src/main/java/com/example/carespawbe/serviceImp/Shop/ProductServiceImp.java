package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.request.ProductVarriantRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.mapper.Shop.ProductMapper;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.Shop.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ImageProductRepository imageProductRepository;
    @Autowired
    private VarriantRepository varriantRepository;
    @Autowired
    private ProductVarriantRepository productVarriantRepository;
    @Autowired
    private JwtService jwtService;

    private Long getUserIdFromAuthHeader(String authorizationHeader) {
        String token = authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Missing token");
        }
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
            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // ✅ shop theo token
            ShopEntity shop = getMyShop(authorizationHeader);

            ProductEntity productEntity = new ProductEntity();
            productEntity.setCategory(category);
            productEntity.setShop(shop);
            productEntity.setProductName(request.getProductName());
//            productEntity.setProductDescribe(request.getProductDescribe());
            productEntity.setProductPrice(request.getProductPrice());
//            productEntity.setProductPriceSale(request.getProductPriceSale());
            productEntity.setProductAmount(request.getProductAmount());
            productEntity.setProductStatus(request.getProductStatus());
            productEntity.setProductUsing(request.getProductUsing());
            productEntity.setProductCreatedAt(LocalDate.now());
            productEntity.setProductUpdatedAt(LocalDate.now());

            ProductEntity savedProduct = productRepository.save(productEntity);

            // Upload ảnh
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

            // Upload video
            if (video != null && !video.isEmpty()) {
                Map<String, String> videoResult = cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");
                savedProduct.setProductVideoUrl(videoResult.get("url"));
                savedProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            // Product variants
            if (request.getProductVarriants() != null && !request.getProductVarriants().isEmpty()) {
                List<ProductVarriantEntity> variantEntities = new ArrayList<>();
                for (ProductVarriantRequest vr : request.getProductVarriants()) {
                    VarriantEntity variant = varriantRepository.findById(vr.getVarriantId())
                            .orElseThrow(() -> new RuntimeException("Varriant not found"));

                    ProductVarriantEntity varEntity = new ProductVarriantEntity();
                    varEntity.setProductVarriants(savedProduct);
                    varEntity.setVarriants(variant);
                    varEntity.setProductVarriantValue(vr.getValue());
                    variantEntities.add(varEntity);
                }
                productVarriantRepository.saveAll(variantEntities);
                savedProduct.setProductVarriantList(variantEntities);
            }

            savedProduct = productRepository.save(savedProduct);
            return productMapper.toProductResponse(savedProduct);

        } catch (Exception e) {
            e.printStackTrace();
            // ✅ FIX: không return null
            throw new RuntimeException("Error creating product: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request, MultipartFile[] images, MultipartFile video, String authorizationHeader) {
        try {
            ProductEntity existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            // ✅ shop theo token
            ShopEntity shop = getMyShop(authorizationHeader);

            // ✅ chặn sửa sản phẩm shop khác
            if (existingProduct.getShop() == null ||
                    !existingProduct.getShop().getShopId().equals(shop.getShopId())) {
                throw new RuntimeException("Bạn không có quyền sửa sản phẩm của shop khác");
            }

            existingProduct.setProductName(request.getProductName());
//            existingProduct.setProductDescribe(request.getProductDescribe());
            existingProduct.setProductPrice(request.getProductPrice());
//            existingProduct.setProductPriceSale(request.getProductPriceSale());
            existingProduct.setProductAmount(request.getProductAmount());
            existingProduct.setProductStatus(request.getProductStatus());
            existingProduct.setProductUsing(request.getProductUsing());
            existingProduct.setCategory(category);
            existingProduct.setShop(shop);

            // ảnh mới
            if (images != null && images.length > 0) {
                List<ImageProductEntity> oldImages = imageProductRepository.findByImageProduct(existingProduct);
                for (ImageProductEntity img : oldImages) {
                    if (img.getImagePublicId() != null) {
                        cloudinaryService.deleteImage(img.getImagePublicId());
                    }
                }
                imageProductRepository.deleteAll(oldImages);

                List<ImageProductEntity> imageEntities = new ArrayList<>();
                for (MultipartFile file : images) {
                    Map<String, String> uploadResult =
                            cloudinaryService.uploadImageUrlAndPublicId(file, "products/images");

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

            // video mới
            if (video != null && !video.isEmpty()) {
                if (existingProduct.getProductVideoPublicId() != null) {
                    cloudinaryService.deleteVideo(existingProduct.getProductVideoPublicId());
                }

                Map<String, String> videoResult =
                        cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");

                existingProduct.setProductVideoUrl(videoResult.get("url"));
                existingProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            // variants
            if (request.getProductVarriants() != null) {
                productVarriantRepository.deleteByProductVarriants(existingProduct);

                List<ProductVarriantEntity> variantEntities = new ArrayList<>();
                for (ProductVarriantRequest vr : request.getProductVarriants()) {
                    VarriantEntity variant = varriantRepository.findById(vr.getVarriantId())
                            .orElseThrow(() -> new RuntimeException("Varriant not found"));

                    ProductVarriantEntity varEntity = new ProductVarriantEntity();
                    varEntity.setProductVarriants(existingProduct);
                    varEntity.setVarriants(variant);
                    varEntity.setProductVarriantValue(vr.getValue());
                    variantEntities.add(varEntity);
                }
                productVarriantRepository.saveAll(variantEntities);
                existingProduct.setProductVarriantList(variantEntities);
            }

            // ✅ FIX: cập nhật updatedAt
            existingProduct.setProductUpdatedAt(LocalDate.now());

            ProductEntity saved = productRepository.save(existingProduct);
            return productMapper.toProductResponse(saved);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    public void deleteProduct(Long productId, String authorizationHeader) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ✅ FIX: lấy shop theo token
        ShopEntity myShop = getMyShop(authorizationHeader);

        // ✅ FIX: chỉ xóa nếu thuộc shop của mình
        if (product.getShop() == null || !product.getShop().getShopId().equals(myShop.getShopId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm của shop khác");
        }

        imageProductRepository.deleteByImageProduct(product);
        productVarriantRepository.deleteByProductVarriants(product);
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getNewProducts() {
        List<ProductEntity> newProducts = productRepository.findTop6ByOrderByProductCreatedAtDesc();
        return newProducts.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getAllProductsByShopId(Long shopId) {
        List<ProductEntity> productEntities =
                productRepository.findAllByShop_ShopIdOrderByProductCreatedAtDesc(shopId);

        return productEntities.stream()
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
        List<ProductEntity> products = productRepository.findProductsByCategoryId(categoryId);

        return products.stream()
                .map(productMapper::toProductResponse) // đổi đúng tên hàm mapper của bạn
                .collect(Collectors.toList());
    }
}
