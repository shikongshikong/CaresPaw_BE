package com.example.carespawbe.dto.Admin;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserItemResponse {
    private Long id;
    private String fullname;
    private String email;
    private String role;
    private String state;
}
