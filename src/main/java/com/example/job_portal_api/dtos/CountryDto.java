package com.example.job_portal_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CountryDto {
    private long id;
    private String iso;

    private String name;

    private String niceName;

    private String iso3;

    private String numcode;

    private String phonecode;

    private String nisCode;
}
