package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AuthenticationService {
    JWTAuthenticationResponse login(LoginRequest loginRequest);

    AdminDTO registerAdmin(AdminDTO dto);

    CompanyDto registerCompany(CompanyDto companyDto) ;

    UserDto registerUser(UserDto userDto);
    void initiatePasswordReset(String email);
    void completePasswordReset(String token, String newPassword);
}
