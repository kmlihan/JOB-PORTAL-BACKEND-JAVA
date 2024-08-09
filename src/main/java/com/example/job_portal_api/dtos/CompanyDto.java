package com.example.job_portal_api.dtos;

import com.example.job_portal_api.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String description;
    private AddressDto address;
    private String websiteUrl;
    private AttachmentDto logo;
    private List<JobDto> jobs;
    private UserType userType;
}
