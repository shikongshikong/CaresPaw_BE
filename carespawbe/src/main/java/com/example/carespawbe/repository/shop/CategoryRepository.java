package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
