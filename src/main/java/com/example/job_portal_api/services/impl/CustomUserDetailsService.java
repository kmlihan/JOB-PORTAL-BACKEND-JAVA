package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.entities.Admin;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.enums.Role;
import com.example.job_portal_api.enums.UserType;
import com.example.job_portal_api.repositories.AdminRepository;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements  UserDetailsService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Try to find Admin
        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if (admin != null && admin.getUserType() == UserType.ADMIN) {
            return User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .roles(Role.ADMIN.name())
                    .build();
        }

        // Try to find User
        com.example.job_portal_api.entities.User user = userRepository.findByEmail(username).orElse(null);
        if (user != null && user.getUserType() == UserType.USER) {
            return User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(Role.USER.name())
                    .build();
        }

        // Try to find Company
        Company company = companyRepository.findByEmail(username).orElse(null);
        if (company != null && company.getUserType() == UserType.COMPANY) {
            return User.builder()
                    .username(company.getEmail())
                    .password(company.getPassword())
                    .roles(Role.COMPANY.name())
                    .build();
        }

        throw new UsernameNotFoundException("User not found with email: " + username);
    }
}
