package com.example.carespawbe.dto.Shop.response;

import com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTrainDBResponse {
    List<ProductInfoDTO> productDb;
    List<UserProductOrderTimeDTO> purchaseDb;
}
