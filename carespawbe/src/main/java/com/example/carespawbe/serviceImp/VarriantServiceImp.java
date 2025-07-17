package com.example.carespawbe.serviceImp;

import com.example.carespawbe.entity.shop.VarriantEntity;
import com.example.carespawbe.repository.shop.VarriantRepository;
import com.example.carespawbe.service.VarriantService;
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
