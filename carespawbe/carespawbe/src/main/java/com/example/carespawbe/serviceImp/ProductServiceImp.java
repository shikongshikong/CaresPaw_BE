package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.entity.CategoryEntity;
import com.example.carespawbe.entity.ProductEntity;
import com.example.carespawbe.entity.ShopEntity;
import com.example.carespawbe.repository.CategoryRepository;
import com.example.carespawbe.repository.ProductRepository;
import com.example.carespawbe.repository.ShopRepository;
import com.example.carespawbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ShopRepository shopRepository;

    @Autowired
    public ProductServiceImp(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ShopRepository shopRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public ProductEntity createProduct(ProductRequest dto) {
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ShopEntity shop = shopRepository.findById(dto.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        ProductEntity product = ProductEntity.builder()
                .name(dto.getName())
                .describe(dto.getDescribe())
                .price(dto.getPrice())
                .amount(dto.getAmount())
                .status(dto.getStatus())
                .using(dto.getUsing())
                .category(category)
                .shop(shop)
                .build();

        return productRepository.save(product);
    }
}
