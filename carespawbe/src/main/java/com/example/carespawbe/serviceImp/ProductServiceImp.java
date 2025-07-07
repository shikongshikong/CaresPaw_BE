package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.request.ProductVarriantRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.entity.shop.*;
import com.example.carespawbe.mapper.ProductMapper;
import com.example.carespawbe.repository.shop.*;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @Override
    public ProductResponse createProduct(ProductRequest request, MultipartFile[] images, MultipartFile video) {
        try {
            // Kiểm tra số lượng ảnh
            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            ShopEntity shop = shopRepository.findById(request.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));

            ProductEntity productEntity = new ProductEntity();
            productEntity.setCategory(category);
            productEntity.setShop(shop);
            productEntity.setProductName(request.getProductName());
            productEntity.setProductDescribe(request.getProductDescribe());
            productEntity.setProductPrice(request.getProductPrice());
            productEntity.setProductAmount(request.getProductAmount());
            productEntity.setProductStatus(request.getProductStatus());
            productEntity.setProductUsing(request.getProductUsing());

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

            return productMapper.toProductResponse(savedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductRequest request, MultipartFile[] images, MultipartFile video) {
        try {
            ProductEntity existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            // Kiểm tra số lượng ảnh
            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

            // Cập nhật thông tin cơ bản
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            ShopEntity shop = shopRepository.findById(request.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found"));

            existingProduct.setProductName(request.getProductName());
            existingProduct.setProductDescribe(request.getProductDescribe());
            existingProduct.setProductPrice(request.getProductPrice());
            existingProduct.setProductAmount(request.getProductAmount());
            existingProduct.setProductStatus(request.getProductStatus());
            existingProduct.setProductUsing(request.getProductUsing());
            existingProduct.setCategory(category);
            existingProduct.setShop(shop);

            //Chỉ xử lý khi có ảnh mới được upload
            if (images != null && images.length > 0) {
                // Xóa ảnh cũ khỏi Cloudinary
                List<ImageProductEntity> oldImages = imageProductRepository.findByImageProduct(existingProduct);
                for (ImageProductEntity img : oldImages) {
                    if (img.getImagePublicId() != null) {
                        cloudinaryService.deleteImage(img.getImagePublicId());
                    }
                }
                // Xóa khỏi DB
                imageProductRepository.deleteAll(oldImages);

                // Upload ảnh mới
                List<ImageProductEntity> imageEntities = new ArrayList<>();
                for (MultipartFile file : images) {
                    Map<String, String> uploadResult = cloudinaryService.uploadImageUrlAndPublicId(file,"products/images");
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

            if (video != null && !video.isEmpty()) {
                // Xóa video cũ nếu có
                if (existingProduct.getProductVideoPublicId() != null) {
                    cloudinaryService.deleteVideo(existingProduct.getProductVideoPublicId());
                }

                // Upload video mới
                Map<String, String> videoResult = cloudinaryService.uploadVideoUrlAndPublicId(video, "products/videos");
                existingProduct.setProductVideoUrl(videoResult.get("url"));
                existingProduct.setProductVideoPublicId(videoResult.get("public_id"));
            }

            // Xử lý product variants
            if (request.getProductVarriants() != null) {
                // Xóa variants cũ
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

            ProductEntity saved = productRepository.save(existingProduct);
            return productMapper.toProductResponse(saved);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating product");
        }
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponse(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Xóa các entity phụ nếu cần (hoặc để JPA cascade handle)
        imageProductRepository.deleteByImageProduct(product);
        productVarriantRepository.deleteByProductVarriants(product);

        productRepository.delete(product); // hoặc set status = -1 nếu dùng xóa mềm
    }

}
