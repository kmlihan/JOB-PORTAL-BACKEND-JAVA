package com.example.job_portal_api.dtos;

import com.example.job_portal_api.entities.Address;
import com.example.job_portal_api.enums.ContractType;
import com.example.job_portal_api.enums.JobType;
import com.example.job_portal_api.enums.LevelOfEducation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private UUID id;
    private String title;
    private String description;
    private double experience;
    private double salary;
    private List<String> skills;
    private ContractType contractType;
    private UUID companyId;
    private String companyName;
    private String companyLogo;
    private JobType jobType;
    private LevelOfEducation levelOfEducation;
    private List<UserDto> applicants;
    private AddressDto address;
    private List<String> responsibilities;
    private List<String> benefits;
    private List<String> languages;
    private boolean archived;
    private boolean isFeatured;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdDate ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updatedDate;


}
