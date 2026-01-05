package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExpertAppListRequest {
    private int page;
    private int pageSize;
    private String keyword;
    private int status;
    private String sort;
}
