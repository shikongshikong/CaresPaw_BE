package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.request.ProductVarriantRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.entity.shop.*;
import com.example.carespawbe.mapper.ProductMapper;
import com.example.carespawbe.repository.shop.*;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
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
            //Kiểm tra số lượng ảnh
            if (images != null && images.length > 9) {
                throw new RuntimeException("Chỉ được upload tối đa 9 ảnh");
            }

            System.out.println("Request categoryId: " + request.getCategoryId());
            System.out.println("Request shopId: " + request.getShopId());

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

            if (images != null && images.length > 0) {
                List<ImageProductEntity> imageEntities = new ArrayList<>();
                for (MultipartFile file : images) {
                    String imageUrl = cloudinaryService.uploadImage(file);
                    ImageProductEntity imageEntity = new ImageProductEntity();
                    imageEntity.setImageProduct(savedProduct);
                    imageEntity.setImageProductUrl(imageUrl);
                    imageEntity.setUploadedAt(LocalDateTime.now());
                    imageEntities.add(imageEntity);
                }
                imageProductRepository.saveAll(imageEntities);
                savedProduct.setImageProductList(imageEntities);
            }

//            System.out.println("Check video: " + (video == null ? "null" : video.getOriginalFilename()));
            if (video != null && !video.isEmpty()) {
                String videoUrl = cloudinaryService.uploadVideo(video);
                System.out.println("Video URL: " + videoUrl);
                savedProduct.setProductVideoUrl(videoUrl);
            }

            // Handle product variants
            if (request.getProductVarriants() != null && !request.getProductVarriants().isEmpty()) {
                List<ProductVarriantEntity> variantEntities = new ArrayList<>();
                for (ProductVarriantRequest vr : request.getProductVarriants()) {
                    VarriantEntity variant = varriantRepository.findById(vr.getVarriantId())
                            .orElseThrow(() -> new RuntimeException("Varriant not found"));

                    ProductVarriantEntity varEntity = new ProductVarriantEntity();
                    varEntity.setProductVarriants(savedProduct);
                    varEntity.setVarriants(variant);
                    varEntity.setProductVarriantValue(vr.getValue());
//                    varEntity.setProductVarriantType(vr.getType());

                    variantEntities.add(varEntity);
                }
                productVarriantRepository.saveAll(variantEntities);
                savedProduct.setProductVarriantList(variantEntities);
            }

            return productMapper.toProductResponse(savedProduct);
        } catch (Exception e) {
//            throw new RuntimeException("Error creating product", e);
            e.printStackTrace();
        }
        return null;
    }

}
