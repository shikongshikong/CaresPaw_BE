package com.example.carespawbe.entity.Auth;

import com.example.carespawbe.entity.Shop.OrderEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name="receiver_name", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String receiverName;

    @Column(name="receiver_phone", nullable = false)
    private String receiverPhone;

    private Integer provinceId;
    private Integer provinceCode;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String provinceName;

    private Integer districtId;
    private Integer districtCode;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String districtName;

    private String wardCode;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String wardName;

    @Column(name = "detail", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String detail;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "shippingAddress", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<OrderEntity> orders = new ArrayList<>();
}

