package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.CompanyDto;
import com.example.job_portal_api.dtos.CompanySearchCriteria;
import com.example.job_portal_api.entities.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CompanyService {
    Page<CompanyDto> getAllCompanies(CompanySearchCriteria criteria, Pageable pageable);

    CompanyDto getCompanyById(UUID id);

    CompanyDto createCompany(CompanyDto companyDto);

    CompanyDto updateCompany(UUID id, CompanyDto companyDto);

    void deleteCompany(UUID id);

    CompanyDto uploadLogo(UUID companyId, MultipartFile logo) throws IOException;

    List<CompanyDto> getAllCompaniesList();

    List<Job> getJobsCompany(UUID companyId);
}
