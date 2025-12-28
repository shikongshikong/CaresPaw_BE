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

        if (request.getValueName() != null) e.setValueName(request.getValueName());
        if (request.getIsActive() != null) e.setIsActive(request.getIsActive());

        return toRes(valueRepo.save(e));
    }

    @Override
    public void delete(Long id) {
        if (!valueRepo.existsById(id)) throw new RuntimeException("Không tìm thấy varriantValueId để xóa!");
        valueRepo.deleteById(id);
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
