package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.CompanyDto;
import com.example.job_portal_api.dtos.CompanySearchCriteria;
import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.exceptions.EmailNotFoundException;
import com.example.job_portal_api.mappers.CompanyMapper;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.specifications.CompanySpecifications;
import com.example.job_portal_api.services.AccessService;
import com.example.job_portal_api.services.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private AccessService accessService;

    @Override
    public List<CompanyDto> getAllCompaniesList() {
        List<Company> companies = companyRepository.findAll();
        List<CompanyDto> dtos = companyMapper.mapToDTOs(companies);
        return dtos;
    }
    @Override
    public Page<CompanyDto> getAllCompanies(CompanySearchCriteria criteria, Pageable pageable) {
        if (!accessService.isAdmin()) {
            throw new SecurityException("Access denied");
        }
        Specification<Company> specification = CompanySpecifications.withCriteria(criteria);
        return companyMapper.mapPageToDTOs(companyRepository.findAll(specification, pageable));
    }

    @Override
    public CompanyDto getCompanyById(UUID id) {
        return companyMapper.mapToDTO(companyRepository.findById(id).orElse(null));
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = companyMapper.mapToEntity(companyDto, new Company());
        company = companyRepository.save(company);
        return companyMapper.mapToDTO(company);
    }

    @Override
    public CompanyDto updateCompany(UUID id, CompanyDto companyDto)  {
        if (!accessService.hasCompanyAccess(id)) {
            throw new SecurityException("Access denied");
        }
        Company existingCompany = companyRepository.findById(id).orElse(null);
        if (existingCompany == null) {
            return null;
        }

        Company updatedCompany = companyMapper.mapToEntity(companyDto, existingCompany);
        updatedCompany = companyRepository.save(updatedCompany);
        CompanyDto dto  = companyMapper.mapToDTO(updatedCompany);
        return dto;
    }

    @Override
    public void deleteCompany(UUID id) {
        if (!accessService.hasCompanyAccess(id)) {
            throw new SecurityException("Access denied");
        }
        companyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CompanyDto uploadLogo(UUID companyId, MultipartFile logo) throws IOException {

        Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Attachment logoAttachment = new Attachment();
        logoAttachment.setFileName(logo.getOriginalFilename());
        logoAttachment.setFileType(logo.getContentType());
        logoAttachment.setData(logo.getBytes());

        company.setLogo(logoAttachment);
        companyRepository.save(company);

        return companyMapper.mapToDTO(company);
    }

    public List<Job> getJobsCompany(UUID companyId) {
        Company company  = companyRepository.findById(companyId).orElseThrow( ()-> new EmailNotFoundException("Company does not exists"));
        return company.getJobs();
    }
}
