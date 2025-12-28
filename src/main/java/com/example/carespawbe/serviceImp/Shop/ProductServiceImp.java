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
import java.util.Optional;
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

    // ✅ ĐÃ CẬP NHẬT: Repository để chuẩn hóa dữ liệu varriant_value_id
    @Autowired
    private VarriantValueRepository varriantValueRepository;

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

    // ✅ ĐÃ CẬP NHẬT: Hàm xử lý tách dòng để lưu vào DB chuẩn 1NF (Mỗi giá trị 1 dòng)
    private void saveProductVariants(List<ProductVarriantRequest> requests, ProductEntity savedProduct) {
        if (requests == null) return;

        for (ProductVarriantRequest vr : requests) {
            VarriantEntity variant = varriantRepository.findById(vr.getVarriantId())
                    .orElseThrow(() -> new RuntimeException("Varriant not found with ID: " + vr.getVarriantId()));

            String rawValues = vr.getValue(); // Ví dụ nhận vào: "s,m"

            if (rawValues != null && !rawValues.trim().isEmpty()) {
                // Tách chuỗi bằng dấu phẩy
                String[] splitValues = rawValues.split(",");

                for (String val : splitValues) {
                    String cleanVal = val.trim();
                    if (cleanVal.isEmpty()) continue;

                    // Tạo thực thể mới cho MỖI giá trị đơn lẻ
                    ProductVarriantEntity varEntity = new ProductVarriantEntity();
                    varEntity.setProductVarriants(savedProduct);
                    varEntity.setVarriants(variant);
                    varEntity.setProductVarriantValue(cleanVal); // Lưu "s" hoặc "m"

                    // Tìm ID chính xác từ bảng varriant_value để cột varriant_value_id KHÔNG bị NULL
                    varriantValueRepository.findByValueNameAndVarriant_VarriantId(cleanVal, variant.getVarriantId())
                            .ifPresent(varEntity::setVarriantValue);

                    // Lưu trực tiếp từng dòng vào database
                    productVarriantRepository.save(varEntity);
                }
            }
        }
    }

    @Override
    public ProductResponse createProduct(ProductRequest request, MultipartFile[] images, MultipartFile video, String authorizationHeader) {
        try {
            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

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

            // Xử lý Upload ảnh
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

            // Xử lý Upload video
            if (video != null && !video.isEmpty()) {
                Map<String, String> videoResult = cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");
                savedProduct.setProductVideoUrl(videoResult.get("url"));
                savedProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            // ✅ GỌI HÀM LƯU BIẾN THỂ TÁCH DÒNG
            if (request.getProductVarriants() != null && !request.getProductVarriants().isEmpty()) {
                saveProductVariants(request.getProductVarriants(), savedProduct);
            }

            return productMapper.toProductResponse(productRepository.save(savedProduct));

        } catch (Exception e) {
            e.printStackTrace();
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

            // Xử lý ảnh mới
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
            }

            // ✅ CẬP NHẬT BIẾN THỂ TÁCH DÒNG
            if (request.getProductVarriants() != null) {
                productVarriantRepository.deleteByProductVarriants(existingProduct);
                saveProductVariants(request.getProductVarriants(), existingProduct);
            }

            return productMapper.toProductResponse(productRepository.save(existingProduct));

        } catch (Exception e) {
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
        ShopEntity myShop = getMyShop(authorizationHeader);

        if (product.getShop() == null || !product.getShop().getShopId().equals(myShop.getShopId())) {
            throw new RuntimeException("Bạn không có quyền xóa sản phẩm này");
        }

        imageProductRepository.deleteByImageProduct(product);
        productVarriantRepository.deleteByProductVarriants(product);
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
}