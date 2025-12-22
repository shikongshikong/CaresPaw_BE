package com.example.carespawbe.serviceImp.Auth;

import com.example.carespawbe.dto.Auth.UserAddressRequest;
import com.example.carespawbe.dto.Auth.UserAddressResponse;
import com.example.carespawbe.entity.Auth.UserAddressEntity;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.repository.Auth.UserAddressRepository;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.service.Auth.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAddressServiceImp implements UserAddressService {

    private final UserAddressRepository repo;
    private final UserRepository userRepo; // ✅ để lấy UserEntity theo userId

    @Override
    public List<UserAddressResponse> list(Long userId) {
        return repo.findByUser_IdAndIsDeletedFalseOrderByIsDefaultDescAddressIdDesc(userId)
                .stream()
                .map(this::toRes)
                .toList();
    }

    @Override
    public UserAddressResponse get(Long userId, Long addressId) {
        UserAddressEntity e = repo.findByAddressIdAndUser_IdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        return toRes(e);
    }

    @Override
    public UserAddressResponse create(Long userId, UserAddressRequest req) {
        validate(req);

        if (req.isDefault()) {
            clearDefaultSimple(userId);
        }

        UserEntity user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserAddressEntity e = new UserAddressEntity();
        e.setUser(user);          // ✅ đúng entity
        e.setDeleted(false);      // ✅ boolean primitive
        apply(e, req);

        return toRes(repo.save(e));
    }

    @Override
    public UserAddressResponse update(Long userId, Long addressId, UserAddressRequest req) {
        validate(req);

        UserAddressEntity e = repo.findByAddressIdAndUser_IdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (req.isDefault()) {
            clearDefaultSimple(userId);
        }

        apply(e, req);
        return toRes(repo.save(e));
    }

    @Override
    public void delete(Long userId, Long addressId) {
        UserAddressEntity e = repo.findByAddressIdAndUser_IdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        e.setDeleted(true);

        // nếu xoá default thì bỏ default luôn (tuỳ chọn)
        if (e.isDefault()) {
            e.setDefault(false);
        }
    }

//    @Transactional
    @Override
    public UserAddressResponse setDefault(Long userId, Long addressId) {
        UserAddressEntity e = repo.findByAddressIdAndUser_IdAndIsDeletedFalse(addressId, userId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        clearDefaultSimple(userId);
        e.setDefault(true);

        return toRes(repo.save(e));
    }
//@Transactional
//@Override
//public UserAddressResponse setDefault(Long userId, Long addressId) {
//    repo.clearDefaultAll(userId);
//    int updated = repo.setDefaultOne(userId, addressId);
//    if (updated == 0) throw new RuntimeException("Address not found");
//
//    UserAddressEntity e = repo.findByAddressIdAndUser_IdAndIsDeletedFalse(addressId, userId)
//            .orElseThrow(() -> new RuntimeException("Address not found"));
//    return toRes(e);
//}


    // ----------------- helper -----------------

    private void clearDefaultSimple(Long userId) {
        repo.findFirstByUser_IdAndIsDeletedFalseAndIsDefaultTrue(userId)
                .ifPresent(addr -> addr.setDefault(false));
    }

    private void apply(UserAddressEntity e, UserAddressRequest req) {
        e.setReceiverName(req.getReceiverName().trim());
        e.setReceiverPhone(req.getReceiverPhone().trim());


        e.setProvinceId(req.getProvinceId());
        e.setProvinceCode(req.getProvinceCode());
        e.setProvinceName(req.getProvinceName());

        e.setDistrictId(req.getDistrictId());
        e.setDistrictCode(req.getDistrictCode());
        e.setDistrictName(req.getDistrictName());

        e.setWardCode(req.getWardCode());
        e.setWardName(req.getWardName());

        // ✅ detail user nhập (ví dụ: "12 Nguyễn Huệ")
        String streetDetail = req.getDetail() == null ? "" : req.getDetail().trim();

        // ✅ detail lưu DB = "12 Nguyễn Huệ, Bến Nghé, Quận 1, TP. HCM"
        String fullDetail = buildFullDetail(
                streetDetail,
                req.getWardName(),
                req.getDistrictName(),
                req.getProvinceName()
        );

        e.setDetail(fullDetail);
        e.setDefault(req.isDefault());
    }

    private void validate(UserAddressRequest req) {
        if (req.getReceiverName() == null || req.getReceiverName().trim().isEmpty())
            throw new RuntimeException("receiverName required");

        if (req.getReceiverPhone() == null || req.getReceiverPhone().trim().isEmpty())
            throw new RuntimeException("receiverPhone required");

        if (req.getProvinceCode() == null || req.getProvinceId() == null || req.getProvinceName() == null || req.getProvinceName().trim().isEmpty())
            throw new RuntimeException("province required");

        if (req.getDistrictCode() == null || req.getDistrictId() == null|| req.getDistrictName() == null || req.getDistrictName().trim().isEmpty())
            throw new RuntimeException("district required");

        if (req.getWardCode() == null || req.getWardName() == null || req.getWardName().trim().isEmpty())
            throw new RuntimeException("ward required");

        if (req.getDetail() == null || req.getDetail().trim().isEmpty())
            throw new RuntimeException("detail required");
    }

    private String buildFullDetail(String street, String ward, String district, String province) {
        StringBuilder sb = new StringBuilder();

        if (street != null && !street.isBlank()) sb.append(street.trim());
        if (ward != null && !ward.isBlank()) sb.append(sb.length() > 0 ? ", " : "").append(ward.trim());
        if (district != null && !district.isBlank()) sb.append(sb.length() > 0 ? ", " : "").append(district.trim());
        if (province != null && !province.isBlank()) sb.append(sb.length() > 0 ? ", " : "").append(province.trim());

        return sb.toString();
    }

    private UserAddressResponse toRes(UserAddressEntity e) {
        return UserAddressResponse.builder()
                .addressId(e.getAddressId())
                .userId(e.getUser() != null ? e.getUser().getId() : null)
                .receiverName(e.getReceiverName())
                .receiverPhone(e.getReceiverPhone())
                .provinceId(e.getProvinceId())
                .provinceCode(e.getProvinceCode())
                .provinceName(e.getProvinceName())
                .districtId(e.getDistrictId())
                .districtCode(e.getDistrictCode())
                .districtName(e.getDistrictName())
                .wardCode(e.getWardCode())
                .wardName(e.getWardName())
                .detail(e.getDetail())
                .isDefault(e.isDefault())
                .isDeleted(e.isDeleted())
                .build();
    }
}
