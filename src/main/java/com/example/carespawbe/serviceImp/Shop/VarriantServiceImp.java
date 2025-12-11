package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.entity.Shop.VarriantEntity;
import com.example.carespawbe.repository.Shop.VarriantRepository;
import com.example.carespawbe.service.Shop.VarriantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VarriantServiceImp implements VarriantService {
    private final VarriantRepository varriantRepository;

    @Override
    public List<VarriantEntity> getAllVarriants() {
        return varriantRepository.findAll();
    }
}
