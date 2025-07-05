package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
