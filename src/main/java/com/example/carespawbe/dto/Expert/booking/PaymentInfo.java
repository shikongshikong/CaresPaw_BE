package com.example.carespawbe.dto.Expert.booking;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentInfo {
    private String method;   // vnpay | momo | cash
    private Boolean agreed;
//    private Integer coinUsed; // optional
}


