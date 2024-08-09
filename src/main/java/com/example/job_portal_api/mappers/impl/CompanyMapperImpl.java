package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.CompanyDto;
import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.entities.Address;
import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.mappers.AddressMapper;
import com.example.job_portal_api.mappers.AttachmentMapper;
import com.example.job_portal_api.mappers.CompanyMapper;
import com.example.job_portal_api.mappers.JobMapper;
import com.example.job_portal_api.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapperImpl extends BaseMapperImpl<Company, CompanyDto> implements CompanyMapper {
    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    @Lazy
    private JobMapper jobMapper;

    @Override
    public CompanyDto mapToDTO(Company company) {
        if (company == null) {
            return null;
        }
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setAddress(addressMapper.mapToDTO(company.getAddress()));
        dto.setDescription(company.getDescription());
        dto.setLogo(attachmentMapper.mapToDTO(company.getLogo()));
        dto.setWebsiteUrl(company.getWebsiteUrl());
        dto.setEmail(company.getEmail());
        if (company.getJobs() != null) {
            dto.setJobs(jobMapper.mapToDTOs(company.getJobs()));
        }
        dto.setUserType(company.getUserType());
        return dto;
    }

    @Override
    public Company mapToEntity(CompanyDto dto, Company company) {
        if (dto == null) {
            return null;
        }

        if (dto.getName() != null) {
            company.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            company.setDescription(dto.getDescription());
        }
        if (dto.getEmail() != null) {
            company.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            company.setPassword(dto.getPassword());
        }
        if (dto.getAddress() != null) {
            company.setAddress(addressMapper.mapToEntity(dto.getAddress(), new Address()));
        }
        if (dto.getWebsiteUrl() != null) {
            company.setWebsiteUrl(dto.getWebsiteUrl());
        }
        if (dto.getLogo() != null) {
            company.setLogo(attachmentMapper.mapToEntity(dto.getLogo(), new Attachment()));
        }
        if (dto.getJobs() != null) {
            company.getJobs().clear();
            for (JobDto jobDto : dto.getJobs()) {
                if (jobDto.getId() != null) {
                    Job j = jobRepository.findById(jobDto.getId()).orElse(null);
                    company.getJobs().add(jobMapper.mapToEntity(jobDto, j));

                } else {
                    Job j = new Job();
                    j = jobMapper.mapToEntity(jobDto, j);
                    company.getJobs().add(j);

                }

            }

        }
        if(dto.getUserType() != null) {
            company.setUserType(dto.getUserType());
        }
        return company;
    }
}


