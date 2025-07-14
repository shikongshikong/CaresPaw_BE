package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.VoucherRequest;
import com.example.carespawbe.dto.response.VoucherResponse;
import com.example.carespawbe.entity.shop.VoucherEntity;
import com.example.carespawbe.entity.shop.ShopEntity;
import com.example.carespawbe.mapper.VoucherMapper;
import com.example.carespawbe.repository.shop.ShopRepository;
import com.example.carespawbe.repository.shop.VoucherRepository;
import com.example.carespawbe.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoucherServiceImp implements VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private VoucherMapper voucherMapper;

    @Override
    public VoucherResponse createVoucher(VoucherRequest request) {
        LocalDate today = LocalDate.now();

        // Kiểm tra tên
        if (request.getVoucherName() == null || request.getVoucherName().trim().isEmpty()) {
            throw new RuntimeException("Tên voucher không được để trống");
        }

        // Kiểm tra ngày bắt đầu và kết thúc hợp lệ
        if (request.getStartedAt().isAfter(request.getFinishedAt())) {
            throw new RuntimeException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        // Ngày kết thúc không được trong quá khứ
        if (request.getFinishedAt().isBefore(today)) {
            throw new RuntimeException("Ngày kết thúc không được ở quá khứ");
        }

        // Kiểm tra giá trị voucher
        if (request.getVoucherValue() == null || request.getVoucherValue() <= 0) {
            throw new RuntimeException("Giá trị voucher phải lớn hơn 0");
        }

        // Kiểm tra loại
        if (!request.getVoucherType().equalsIgnoreCase("MONEY")
                && !request.getVoucherType().equalsIgnoreCase("PERCENT")) {
            throw new RuntimeException("Loại voucher phải là MONEY hoặc PERCENT");
        }

        // Nếu là phần trăm, không được quá 100%
        if (request.getVoucherType().equalsIgnoreCase("PERCENT") && request.getVoucherValue() > 100) {
            throw new RuntimeException("Voucher phần trăm không được vượt quá 100%");
        }

        // Kiểm tra số lượng
        if (request.getVoucherAmount() <= 0) {
            throw new RuntimeException("Số lượng voucher phải lớn hơn 0");
        }

        // Kiểm tra Shop tồn tại
        ShopEntity shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Not found shop with id: " + request.getShopId()));

        VoucherEntity voucher = new VoucherEntity();
        voucher.setVoucherName(request.getVoucherName());
        voucher.setVoucherDescribe(request.getVoucherDescribe());
        voucher.setVoucherValue(request.getVoucherValue());
        voucher.setVoucherType(request.getVoucherType());
        voucher.setStartedAt(request.getStartedAt());
        voucher.setFinishedAt(request.getFinishedAt());
        voucher.setVoucherAmount(request.getVoucherAmount());
        voucher.setVoucherStatus(request.getVoucherStatus());
        voucher.setShop(shop);

        // Lưu vào DB
        return voucherMapper.toVoucherResponse(voucherRepository.save(voucher));
    }

    @Override
    public VoucherResponse updateVoucher(Long voucherId, VoucherRequest request) {
        LocalDate today = LocalDate.now();

        VoucherEntity existingVoucher = voucherRepository.findById(voucherId)
                .orElseThrow(() -> new RuntimeException("Not found voucher with id: " + voucherId));

        // Kiểm tra tên
        if (request.getVoucherName() == null || request.getVoucherName().trim().isEmpty()) {
            throw new RuntimeException("Tên voucher không được để trống");
        }

        // Kiểm tra ngày bắt đầu và kết thúc hợp lệ
        if (request.getStartedAt().isAfter(request.getFinishedAt())) {
            throw new RuntimeException("Ngày bắt đầu không được sau ngày kết thúc");
        }

        // Ngày kết thúc không được trong quá khứ
        if (request.getFinishedAt().isBefore(today)) {
            throw new RuntimeException("Ngày kết thúc không được ở quá khứ");
        }

        // Kiểm tra giá trị voucher
        if (request.getVoucherValue() == null || request.getVoucherValue() <= 0) {
            throw new RuntimeException("Giá trị voucher phải lớn hơn 0");
        }

        // Kiểm tra loại
        if (!request.getVoucherType().equalsIgnoreCase("MONEY")
                && !request.getVoucherType().equalsIgnoreCase("PERCENT")) {
            throw new RuntimeException("Loại voucher phải là MONEY hoặc PERCENT");
        }

        // Nếu là phần trăm, không được quá 100%
        if (request.getVoucherType().equalsIgnoreCase("PERCENT") && request.getVoucherValue() > 100) {
            throw new RuntimeException("Voucher phần trăm không được vượt quá 100%");
        }

        // Kiểm tra số lượng
        if (request.getVoucherAmount() <= 0) {
            throw new RuntimeException("Số lượng voucher phải lớn hơn 0");
        }

        // Kiểm tra Shop tồn tại
        ShopEntity shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new RuntimeException("Not found shop with id: " + request.getShopId()));

        existingVoucher.setVoucherName(request.getVoucherName());
        existingVoucher.setVoucherDescribe(request.getVoucherDescribe());
        existingVoucher.setVoucherValue(request.getVoucherValue());
        existingVoucher.setVoucherType(request.getVoucherType());
        existingVoucher.setStartedAt(request.getStartedAt());
        existingVoucher.setFinishedAt(request.getFinishedAt());
        existingVoucher.setVoucherAmount(request.getVoucherAmount());
        existingVoucher.setVoucherStatus(request.getVoucherStatus());
        existingVoucher.setShop(shop);

        // Lưu vào DB
        return voucherMapper.toVoucherResponse(voucherRepository.save(existingVoucher));
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
    public List<VoucherResponse> getAllVouchers() {
        List<VoucherEntity> vouchers = voucherRepository.findAll();
        return vouchers.stream()
                .map(voucherMapper::toVoucherResponse)
                .toList();
    }

    @Override
    public boolean isVoucherValid(String voucherName) {
        Optional<VoucherEntity> optionalVoucher = voucherRepository.findByVoucherName(voucherName);
        if (optionalVoucher.isEmpty()) return false;

        VoucherEntity voucher = optionalVoucher.get();
        LocalDate now = LocalDate.now();

        return voucher.getVoucherStatus() == 1 &&
                now.isAfter(voucher.getStartedAt()) &&
                now.isBefore(voucher.getFinishedAt()) &&
                voucher.getVoucherAmount() > 0;
    }

    @Override
    public List<VoucherResponse> getAllVoucherByShop(Long shopId) {
        List<VoucherEntity> vouchers = voucherRepository.findAllByShop_ShopId(shopId);
        return vouchers.stream()
                .map(voucherMapper::toVoucherResponse)
                .toList();
    }


    // Các hàm update, delete, getById, getAll tương tự như create
}
