package com.example.carespawbe.dto.Expert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShortExpertResponse {
    private Long id;
    private String name;
    private float rating;
    private String major;
    private String experience;
}
