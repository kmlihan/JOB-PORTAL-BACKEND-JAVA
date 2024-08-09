package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.entities.User;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.JobRepository;
import com.example.job_portal_api.repositories.UserRepository;
import com.example.job_portal_api.services.AccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccessServiceImpl implements AccessService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean isCompanyRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_COMPANY"));
    }

    public boolean isUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"));
    }

    public boolean hasUserAccess(UUID userId) {
        if (isAdmin()) {
            return true;
        }

        if (isUserRole()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findById(userId).orElse(null);
            return user != null && user.getEmail().equals(userDetails.getUsername());
        }

        return false;
    }

    public boolean hasCompanyAccess(UUID companyId) {
        if (isAdmin()) {
            return true;
        }

        if (isCompanyRole()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Company company = companyRepository.findById(companyId).orElse(null);
            return company != null && company.getEmail().equals(userDetails.getUsername());
        }

        return false;
    }

    public boolean hasJobAccess(UUID jobId) {
        if (isAdmin()) {
            return true;
        }

        if (isCompanyRole()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Job job = jobRepository.findById(jobId).orElse(null);
            return job != null && job.getCompany().getEmail().equals(userDetails.getUsername());
        }

        return false;
    }
}