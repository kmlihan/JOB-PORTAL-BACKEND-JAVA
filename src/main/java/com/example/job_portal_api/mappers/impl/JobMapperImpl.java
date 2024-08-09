package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.entities.User;
import com.example.job_portal_api.mappers.AddressMapper;
import com.example.job_portal_api.mappers.JobMapper;
import com.example.job_portal_api.mappers.UserMapper;
import com.example.job_portal_api.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.stream.Collectors;

@Component
public class JobMapperImpl extends BaseMapperImpl<Job, JobDto> implements JobMapper {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private AddressMapper addressMapper;


    @Override
    public JobDto mapToDTO(Job job) {
        if (job == null) {
            return null;
        }
        JobDto dto = new JobDto();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setExperience(job.getExperience());
        dto.setSalary(job.getSalary());
        dto.setSkills(job.getSkills());
        dto.setContractType(job.getContractType());
        dto.setCompanyId(job.getCompany() != null ? job.getCompany().getId() : null);
        dto.setJobType(job.getJobType());
        dto.setLevelOfEducation(job.getLevelOfEducation());
        dto.setAddress(addressMapper.mapToDTO(job.getAddress()));
        Company company = companyRepository.findById(job.getCompany().getId()).orElse(null);
        if (company != null) {
           dto.setCompanyName(company.getName());
        }
        if (company != null && company.getLogo() != null) {
            String base64Logo = Base64.getEncoder().encodeToString(company.getLogo().getData());
            dto.setCompanyLogo("data:" + company.getLogo().getFileType() + ";base64," + base64Logo); // assuming the image is JPEG
        }

        if (job.getApplicants() != null) {
            dto.setApplicants(userMapper.mapToDTOs(job.getApplicants()));
        }
        dto.setBenefits(job.getBenefits());
        dto.setResponsibilities(job.getResponsibilities());
        dto.setLanguages(job.getLanguages());
        dto.setArchived(job.isArchived());
        dto.setFeatured(job.isFeatured());
        dto.setCreatedDate(job.getCreatedDate());
        dto.setUpdatedDate(job.getUpdatedDate());
        return dto;

    }

    @Override
    public Job mapToEntity(JobDto dto, Job job) {
        if (dto == null) {
            return null;
        }

        if(dto.getId() != null) {
            job.setId(dto.getId());
        }
        if (dto.getTitle() != null) {
            job.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            job.setDescription(dto.getDescription());
        }


        job.setExperience(dto.getExperience());
        job.setSalary(dto.getSalary());


        if (dto.getSkills() != null) {
            job.setSkills(dto.getSkills());
        }

        if (dto.getContractType() != null) {
            job.setContractType(dto.getContractType());
        }
        if (dto.getCompanyId() != null) {
            Company company = companyRepository.findById(dto.getCompanyId()).orElse(null);
            if (company != null) {
                job.setCompany(company);
            }
        }
        if (dto.getJobType() != null) {
            job.setJobType(dto.getJobType());
        }
        if (dto.getLevelOfEducation() != null) {
            job.setLevelOfEducation(dto.getLevelOfEducation());
        }
        if (dto.getApplicants() != null) {
            job.setApplicants(dto.getApplicants().stream().map(x -> userMapper.mapToEntity(x, new User())).collect(Collectors.toList()));
        }
        if (dto.getAddress() != null) {
            job.setAddress(addressMapper.mapToEntity(dto.getAddress(), job.getAddress()));
        }
        if (dto.getResponsibilities() != null) {
            job.setResponsibilities(dto.getResponsibilities());
        }
        if (dto.getBenefits() != null) {
            job.setBenefits(dto.getBenefits());
        }
        if (dto.getLanguages() != null) {
            job.setLanguages(dto.getLanguages());
        }
        job.setArchived(dto.isArchived());
        job.setFeatured(dto.isFeatured());
        if(dto.getCompanyName() != null && !dto.getCompanyName().isEmpty()) {
            job.setCompanyName(dto.getCompanyName());
        }
        if(dto.getCompanyLogo() != null && !dto.getCompanyLogo().isEmpty()) {
            job.setCompanyName(dto.getCompanyLogo());
        }
        if(dto.getCreatedDate() != null) {
            job.setCreatedDate(dto.getCreatedDate());
        }
        if(dto.getUpdatedDate() != null) {
            job.setUpdatedDate(dto.getUpdatedDate());
        }
        return job;
    }
}
