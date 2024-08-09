package com.example.job_portal_api.dtos;

import com.example.job_portal_api.enums.ContractType;
import com.example.job_portal_api.enums.JobType;
import com.example.job_portal_api.enums.LevelOfEducation;
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
public class JobSearchCriteria {
    private String title;
    private String description;
    private List<String> languages;
    private Double experience;
    private Double salary;
    private List<String> skills;
    private ContractType contractType;
    private UUID companyId;
    private JobType jobType;
    private LevelOfEducation levelOfEducation;
    private String city;
    private String country;
}
