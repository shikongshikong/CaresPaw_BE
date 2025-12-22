package com.example.carespawbe.repository.Auth;

import com.example.carespawbe.entity.Auth.UserAddressEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository extends JpaRepository<UserAddressEntity, Long> {

    List<UserAddressEntity> findByUser_IdAndIsDeletedFalseOrderByIsDefaultDescAddressIdDesc(Long userId);

    Optional<UserAddressEntity> findByAddressIdAndUser_IdAndIsDeletedFalse(Long addressId, Long userId);

    Optional<UserAddressEntity> findFirstByUser_IdAndIsDeletedFalseAndIsDefaultTrue(Long userId);
    @Modifying
    @Query("update UserAddressEntity a set a.isDefault = false where a.user.id = :userId and a.isDeleted = false")
    void clearDefaultAll(@Param("userId") Long userId);

    @Modifying
    @Query("update UserAddressEntity a set a.isDefault = true where a.addressId = :addressId and a.user.id = :userId and a.isDeleted = false")
    int setDefaultOne(@Param("userId") Long userId, @Param("addressId") Long addressId);

}
