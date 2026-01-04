package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.VarriantRequest;
import com.example.carespawbe.dto.Shop.response.VarriantResponse;
import com.example.carespawbe.entity.Shop.VarriantEntity;
import com.example.carespawbe.mapper.Shop.VarriantMapper;
import com.example.carespawbe.repository.Shop.VarriantRepository;
import com.example.carespawbe.service.Shop.VarriantService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarriantServiceImp implements VarriantService {
    @Autowired
    private VarriantRepository varriantRepository;
    @Autowired
    private VarriantMapper varriantMapper;

    @Override
    public List<VarriantResponse> getAllVarriants() {
        List<VarriantEntity> entities = varriantRepository.findAll();
        // MapStruct tự xử lý list
        return varriantMapper.toVarriantResponseList(entities);
    }

    @Override
    public VarriantResponse createVarriant(VarriantRequest request) {
        if (varriantRepository.existsByVarriantName(request.getVarriantName())) {
            throw new RuntimeException("Tên biến thể đã tồn tại!");
        }

        VarriantEntity entity = VarriantEntity.builder()
                .varriantName(request.getVarriantName())
                .build();

        VarriantEntity savedEntity = varriantRepository.save(entity);
        return mapToResponse(savedEntity);
    }

    @Override
    public VarriantResponse updateVarriant(Long id, VarriantRequest request) {
        VarriantEntity entity = varriantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với ID: " + id));

        entity.setVarriantName(request.getVarriantName());

        VarriantEntity updatedEntity = varriantRepository.save(entity);
        return mapToResponse(updatedEntity);
    }

    @Override
    public void deleteVarriant(Long id) {
        if (!varriantRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy biến thể để xóa!");
        }
        // Lưu ý: Nếu biến thể này đang được dùng trong ProductVarriant,
        // bạn có thể gặp lỗi Foreign Key. Cần xử lý xóa ProductVarriant trước hoặc dùng Cascade.
        varriantRepository.deleteById(id);
    }

    @Override
    public VarriantResponse getById(Long id) {
        VarriantEntity entity = varriantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy biến thể với ID: " + id));
        return mapToResponse(entity);
    }

    // Hàm phụ trợ để chuyển Entity sang Response
    private VarriantResponse mapToResponse(VarriantEntity entity) {
        return VarriantResponse.builder()
                .varriantId(entity.getVarriantId())
                .varriantName(entity.getVarriantName())
                .build();
    }
}
