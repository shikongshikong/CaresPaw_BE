package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.VoucherRequest;
import com.example.carespawbe.dto.Shop.response.VoucherResponse;
import com.example.carespawbe.entity.Shop.ShopEntity;
import com.example.carespawbe.entity.Shop.VoucherEntity;
import com.example.carespawbe.mapper.Shop.VoucherMapper;
import com.example.carespawbe.repository.Shop.ShopRepository;
import com.example.carespawbe.repository.Shop.VoucherRepository;
import com.example.carespawbe.service.Shop.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoucherServiceImp implements VoucherService {

    private final VoucherRepository voucherRepository;
    private final ShopRepository shopRepository;
    private final VoucherMapper voucherMapper;

    private String normalizeType(String type) {
        if (type == null) return null;
        String t = type.trim().toUpperCase();
        // FE có thể gửi AMOUNT/MONEY đều được
        if (t.equals("AMOUNT")) return "MONEY";
        return t;
    }

    private void validateVoucherRequest(VoucherRequest request) {
        LocalDate today = LocalDate.now();

        if (request.getVoucherName() == null || request.getVoucherName().trim().isEmpty()) {
            throw new RuntimeException("Tên voucher không được để trống");
        }

        if (request.getStartedAt() == null || request.getFinishedAt() == null) {
            throw new RuntimeException("Ngày bắt đầu/kết thúc không được trống");
        }

        if (request.getStartedAt().isAfter(request.getFinishedAt())) {
            throw new RuntimeException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        if (request.getFinishedAt().isBefore(today)) {
            throw new RuntimeException("Ngày kết thúc không được ở quá khứ");
        }

        if (request.getVoucherValue() == null || request.getVoucherValue() <= 0) {
            throw new RuntimeException("Giá trị voucher phải lớn hơn 0");
        }

        String type = normalizeType(request.getVoucherType());
        if (!"MONEY".equals(type) && !"PERCENT".equals(type)) {
            throw new RuntimeException("Loại voucher phải là MONEY hoặc PERCENT");
        }

        if ("PERCENT".equals(type) && request.getVoucherValue() > 100) {
            throw new RuntimeException("Voucher phần trăm không được vượt quá 100%");
        }

        if (request.getVoucherAmount() <= 0) {
            throw new RuntimeException("Số lượng voucher phải lớn hơn 0");
        }

        // ✅ validate voucherUsageType: 1 = product, 2 = order (hoặc shipping tuỳ bạn dùng)
        if (request.getVoucherUsageType() != 1 && request.getVoucherUsageType() != 2) {
            throw new RuntimeException("Loại sử dụng voucher không hợp lệ (chỉ chấp nhận 1 hoặc 2)");
        }

        // ✅ validate voucherMinOrder: không âm
        if (request.getVoucherMinOrder() < 0) {
            throw new RuntimeException("Giá trị đơn tối thiểu (voucherMinOrder) không được âm");
        }
    }

    @Override
    public VoucherResponse createVoucher(Long shopId, VoucherRequest request) {
        validateVoucherRequest(request);

        ShopEntity shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("Not found shop with id: " + shopId));

        // Optional: chặn trùng tên voucher trong 1 shop
        voucherRepository.findByShop_ShopIdAndVoucherNameIgnoreCase(shopId, request.getVoucherName())
                .ifPresent(v -> {
                    throw new RuntimeException("Voucher name đã tồn tại trong shop");
                });

        VoucherEntity voucher = voucherMapper.toEntity(request);

        // chuẩn hoá type
        voucher.setVoucherType(normalizeType(request.getVoucherType()));

        // ✅ gán 2 field mới (nếu mapper chưa map thì đoạn này đảm bảo đúng)
        voucher.setVoucherUsageType(request.getVoucherUsageType());   // 1 / 2
        voucher.setVoucherMinOrder(request.getVoucherMinOrder());     // >= 0

        voucher.setShop(shop);

        VoucherEntity saved = voucherRepository.save(voucher);
        return voucherMapper.toVoucherResponse(saved);
    }

    @Override
    public VoucherResponse updateVoucher(Long voucherId, VoucherRequest request) {
        validateVoucherRequest(request);

        VoucherEntity existing = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Not found voucher with id: " + voucherId));

        // update fields từ request → entity
        voucherMapper.updateEntity(existing, request);

        existing.setVoucherType(normalizeType(request.getVoucherType()));

        // ✅ cập nhật lại 2 field mới
        existing.setVoucherUsageType(request.getVoucherUsageType());
        existing.setVoucherMinOrder(request.getVoucherMinOrder());

        VoucherEntity saved = voucherRepository.save(existing);
        return voucherMapper.toVoucherResponse(saved);
    }

    @Override
    public void deleteVoucher(Long voucherId) {
        VoucherEntity voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Not found voucher with id: " + voucherId));
        voucherRepository.delete(voucher);
    }

    @Override
    public VoucherResponse getVoucherById(Long voucherId) {
        VoucherEntity voucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy voucher với ID: " + voucherId));
        return voucherMapper.toVoucherResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getAllVoucherByShop(Long shopId) {
        return voucherRepository.findAllByShop_ShopId(shopId)
                .stream()
                .map(voucherMapper::toVoucherResponse)
                .toList();
    }

    @Override
    public List<VoucherResponse> getAvailableVouchersByShop(Long shopId, LocalDate today) {
        LocalDate d = (today == null) ? LocalDate.now() : today;
        return voucherRepository.findAvailableByShop(shopId, d)
                .stream()
                .map(voucherMapper::toVoucherResponse)
                .toList();
    }

    /**
     * Apply voucher lúc checkout cho 1 shop-order:
     * - voucher phải thuộc shopId
     * - voucherStatus != 0
     * - amount > 0
     * - today trong [startedAt, finishedAt]
     * - trừ amount đi 1
     */
    @Override
    @Transactional
    public VoucherResponse applyVoucher(Long shopId, Long voucherId, LocalDate today) {
        LocalDate d = (today == null) ? LocalDate.now() : today;

        // đảm bảo voucher thuộc shop
        VoucherEntity voucher = voucherRepository.findByVoucherIdAndShop_ShopId(voucherId, shopId)
                .orElseThrow(() -> new RuntimeException("Voucher không thuộc shop hoặc không tồn tại"));

        if (voucher.getVoucherStatus() == 0) {
            throw new RuntimeException("Voucher đang bị khóa");
        }
        if (voucher.getVoucherAmount() <= 0) {
            throw new RuntimeException("Voucher đã hết số lượng");
        }
        if (voucher.getStartedAt().isAfter(d) || voucher.getFinishedAt().isBefore(d)) {
            throw new RuntimeException("Voucher không còn hiệu lực theo ngày");
        }

        // ⚠️ Ở đây hiện tại chưa check voucherMinOrder vì method chưa nhận tổng tiền đơn.
        // Sau này nếu bạn truyền thêm orderTotal vào applyVoucher thì có thể check:
        // if (orderTotal < voucher.getVoucherMinOrder()) { ... }

        voucher.setVoucherAmount(voucher.getVoucherAmount() - 1);
        VoucherEntity saved = voucherRepository.save(voucher);

        return voucherMapper.toVoucherResponse(saved);
    }
}
