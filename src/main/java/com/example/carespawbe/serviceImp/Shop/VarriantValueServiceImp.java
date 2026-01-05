package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantValueRequest;
import com.example.carespawbe.dto.Shop.response.VarriantValueResponse;
import com.example.carespawbe.entity.Shop.VarriantEntity;
import com.example.carespawbe.entity.Shop.VarriantValueEntity;
import com.example.carespawbe.repository.Shop.VarriantRepository;
import com.example.carespawbe.repository.Shop.VarriantValueRepository;
import com.example.carespawbe.service.Shop.VarriantValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarriantValueServiceImp implements VarriantValueService {

    private final VarriantValueRepository valueRepo;
    private final VarriantRepository varriantRepo;

    @Override
    public List<VarriantValueResponse> getValuesByVarriantId(Long varriantId) {
        return valueRepo.findByVarriant_VarriantIdAndIsActiveTrue(varriantId)
                .stream()
                .map(this::toRes)
                .toList();
    }

    @Override
    public VarriantValueResponse create(VarriantValueRequest request) {
        VarriantEntity v = varriantRepo.findById(request.getVarriantId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy varriantId: " + request.getVarriantId()));

        if (valueRepo.existsByVarriant_VarriantIdAndValueName(v.getVarriantId(), request.getValueName())) {
            throw new RuntimeException("Giá trị đã tồn tại cho biến thể này!");
        }

        VarriantValueEntity e = VarriantValueEntity.builder()
                .valueName(request.getValueName())
                .isActive(request.getIsActive() == null ? true : request.getIsActive())
                .varriant(v)
                .build();

        return toRes(valueRepo.save(e));
    }

    @Override
    public VarriantValueResponse update(Long id, VarriantValueRequest request) {
        VarriantValueEntity e = valueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy varriantValueId: " + id));

        // nếu đổi tên -> check trùng trong cùng varriant
        if (request.getValueName() != null && !request.getValueName().isBlank()) {
            Long varId = e.getVarriant().getVarriantId();
            String newName = request.getValueName().trim();

            boolean duplicate = valueRepo.existsByVarriant_VarriantIdAndValueName(varId, newName)
                    && !newName.equalsIgnoreCase(e.getValueName());

            if (duplicate) {
                throw new RuntimeException("Giá trị đã tồn tại cho biến thể này!");
            }
            e.setValueName(newName);
        }

        if (request.getIsActive() != null) e.setIsActive(request.getIsActive());

        return toRes(valueRepo.save(e));
    }

    @Override
    public void delete(Long id) {
        if (!valueRepo.existsById(id)) throw new RuntimeException("Không tìm thấy varriantValueId để xóa!");
        valueRepo.deleteById(id);
    }

    // ✅ DONE: getById
    @Override
    public VarriantValueResponse getById(Long id) {
        VarriantValueEntity e = valueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy varriantValueId: " + id));
        return toRes(e);
    }

    // ✅ DONE: toggleActive
    @Override
    public void toggleActive(Long id, Boolean isActive) {
        if (isActive == null) {
            throw new RuntimeException("isActive is required");
        }
        VarriantValueEntity e = valueRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy varriantValueId: " + id));

        e.setIsActive(isActive);
        valueRepo.save(e);
    }

    private VarriantValueResponse toRes(VarriantValueEntity e) {
        return VarriantValueResponse.builder()
                .varriantValueId(e.getVarriantValueId())
                .valueName(e.getValueName())
                .isActive(e.getIsActive())
                .varriantId(e.getVarriant().getVarriantId())
                .varriantName(e.getVarriant().getVarriantName())
                .build();
    }
}
