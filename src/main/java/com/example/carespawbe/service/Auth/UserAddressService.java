package com.example.carespawbe.service.Auth;

import com.example.carespawbe.dto.Auth.UserAddressRequest;
import com.example.carespawbe.dto.Auth.UserAddressResponse;

import java.util.List;

public interface UserAddressService {
    List<UserAddressResponse> list(Long userId);

    UserAddressResponse get(Long userId, Long addressId);

    UserAddressResponse create(Long userId, UserAddressRequest req);

    UserAddressResponse update(Long userId, Long addressId, UserAddressRequest req);

    void delete(Long userId, Long addressId);

    UserAddressResponse setDefault(Long userId, Long addressId);
}
