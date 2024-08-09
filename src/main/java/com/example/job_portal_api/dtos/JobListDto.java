package com.example.job_portal_api.dtos;

import com.example.job_portal_api.enums.ContractType;
import com.example.job_portal_api.enums.JobType;
import com.example.job_portal_api.enums.LevelOfEducation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;
@Data
@Jacksonized
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobListDto {
    private UUID id;
    private String title;
    private String description;
    private String language;
    private double experience;
    private double minSalary;
    private double maxSalary;
    private List<String> skills;
    private List<String> roles;
    private ContractType contractType;
    private java.util.UUID companyId;
    private JobType jobType;
    private LevelOfEducation levelOfEducation;
    private List<UserDto> applicants;
    private AddressDto address;
}
