package com.example.job_portal_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private UUID id;
    private String city;
    private String postalCode;
    private String street;
    private String houseNumber;
    private String box;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String country;
}
