package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.repository.Shop.VarriantRepository;
import com.example.carespawbe.repository.Shop.VarriantValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/shop/varriants")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ShopVarriantController {

    private final VarriantRepository varriantRepository;
    private final VarriantValueRepository varriantValueRepository;

    @GetMapping("/with-values")
    public ResponseEntity<?> getVarriantsWithValues() {

        var result = varriantRepository.findAll()
                .stream()
                .map(v -> Map.of(
                        "varriantId", v.getVarriantId(),
                        "varriantName", v.getVarriantName(),
                        "values",
                        varriantValueRepository
                                .findByVarriant_VarriantIdAndIsActiveTrue(v.getVarriantId())
                                .stream()
                                .map(val -> Map.of(
                                        "varriantValueId", val.getVarriantValueId(),
                                        "valueName", val.getValueName()
                                ))
                                .toList()
                ))
                .toList();

        return ResponseEntity.ok(result);
    }
}
