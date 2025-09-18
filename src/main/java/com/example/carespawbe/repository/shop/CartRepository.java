package com.example.carespawbe.repository.shop;

import com.example.carespawbe.entity.shop.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
      CartEntity findCartEntityByUserEntity_Id(Long id);



//    Optional<CartEntity> findByUserUserId(Long userId);
}
