package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.entity.Shop.VarriantEntity;
import com.example.carespawbe.service.Shop.VarriantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/varriants")
@RequiredArgsConstructor
public class VarriantController {

    private final VarriantService varriantService;

    @GetMapping
    public ResponseEntity<List<VarriantEntity>> getAllVarriants() {
        return ResponseEntity.ok(varriantService.getAllVarriants());
    }
}
