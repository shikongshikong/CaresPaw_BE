package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.ShopRequest;
import com.example.carespawbe.dto.response.ShopResponse;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.entity.shop.ShopEntity;
import com.example.carespawbe.mapper.ShopMapper;
import com.example.carespawbe.repository.UserRepository;
import com.example.carespawbe.repository.shop.ShopRepository;
import com.example.carespawbe.service.CloudinaryService;
import com.example.carespawbe.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShopServiceImp implements ShopService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private ShopMapper shopMapper;

    @Override
    public ShopResponse registerShop(ShopRequest request, MultipartFile shopLogo) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
//        shopRepository.findByUserId(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User have registered shop"));

        // Nếu đã có shop rồi thì không cho đăng ký nữa
        if (shopRepository.findByUserId(request.getUserId()).isPresent()) {
            throw new RuntimeException("User have registered shop");
        }

        // Cập nhật phone nếu có thay đổi
        user.setPhoneNumber(request.getShopPhone());
        user.setRole(2);
        userRepository.save(user);

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopName(request.getShopName());
        shopEntity.setShopAddress(request.getShopAddress());
        shopEntity.setUser(user);
        shopEntity.setCreated_at(LocalDateTime.now());
        shopEntity.setShopAmountFollower(0);

        if (shopLogo != null) {
            String imageUrl = cloudinaryService.uploadImage(shopLogo);
            shopEntity.setShopLogo(imageUrl);
        }
        return shopMapper.toResponse(shopRepository.save(shopEntity));
    }

}
