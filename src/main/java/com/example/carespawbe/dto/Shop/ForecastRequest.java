package com.example.carespawbe.dto.Shop;

import lombok.*;
 import jakarta.validation.constraints.*; // nếu Spring Boot 3
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastRequest {

//    @NotNull(message = "data không được null")
//    @Size(min = 2, message = "data phải có ít nhất 2 phần tử")
    private List<Double> data;

//    @Min(value = 0, message = "predictN phải >= 0")
//    @Builder.Default
    private Integer predictN = 5;

    // nếu null -> dùng floor(0.8 * n)
    private Integer trainLen;
}
