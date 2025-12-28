package com.example.carespawbe.repository.Shop;

import com.example.carespawbe.entity.Shop.VarriantValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VarriantValueRepository extends JpaRepository<VarriantValueEntity, Long> {
    List<VarriantValueEntity> findByVarriant_VarriantIdAndIsActiveTrue(Long varriantId);
    boolean existsByVarriant_VarriantIdAndValueName(Long varriantId, String valueName);
    Optional<VarriantValueEntity> findByValueNameAndVarriant_VarriantId(String valueName, Long varriantId);
}
