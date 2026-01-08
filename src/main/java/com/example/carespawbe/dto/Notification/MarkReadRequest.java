package com.example.carespawbe.dto.Notification;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarkReadRequest {
    private List<Long> ids;
}