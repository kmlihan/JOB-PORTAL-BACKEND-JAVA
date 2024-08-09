package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.*;
import com.example.job_portal_api.entities.Admin;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.User;
import com.example.job_portal_api.exceptions.EmailNotFoundException;
import com.example.job_portal_api.mappers.AdminMapper;
import com.example.job_portal_api.mappers.CompanyMapper;
import com.example.job_portal_api.mappers.UserMapper;
import com.example.job_portal_api.repositories.AdminRepository;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.UserRepository;
import com.example.job_portal_api.services.AuthenticationService;
import com.example.job_portal_api.services.CompanyService;
import com.example.job_portal_api.services.JWTService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    private final CustomUserDetailsService userDetailsService;

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final AdminMapper adminMapper;
    private final CompanyMapper companyMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final JavaMailSender mailSender;
    @Value("${reset.password.url}")
    private String resetPasswordUrl;
    @PostConstruct
    public void initDefaultAdmin() {
        String defaultAdminEmail = "admin@example.com";
        if (!adminRepository.findByEmail(defaultAdminEmail).isPresent()) {
            Admin admin = new Admin();
            admin.setEmail(defaultAdminEmail);
            admin.setPassword(passwordEncoder.encode("defaultAdminPassword"));
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            adminRepository.save(admin);
        }
    }
    public AdminDTO registerAdmin(AdminDTO dto) {
        Admin admin = adminMapper.mapToEntity(dto, new Admin());
        admin.setPassword((passwordEncoder.encode(admin.getPassword())));
        return adminMapper.mapToDTO(adminRepository.save(admin));
    }

    @Transactional
    public CompanyDto registerCompany(CompanyDto companyDto)  {
        Company company = companyMapper.mapToEntity(companyDto, new Company());
        company.setPassword(passwordEncoder.encode(company.getPassword()));
        company = companyRepository.save(company);
        CompanyDto dto = companyMapper.mapToDTO(company);
        return dto;
    }

    public UserDto registerUser(UserDto userDto) {
        User user = userMapper.mapToEntity(userDto, new User());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.mapToDTO(userRepository.save(user));
    }

    public JWTAuthenticationResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        String role = userDetails.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        String id = "";
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        Company company = companyRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        Admin admin = adminRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if(user != null) {
            id = user.getId().toString();
        }
        if (company != null) {
            id = company.getId().toString();
        }
        if (admin != null) {
            id = admin.getId().toString();
        }

        String jwt = jwtService.generateToken(userDetails, role, id);
        String refreshToken = jwtService.generateRefreshToken(new HashMap<>(), userDetails);
        JWTAuthenticationResponse jwtAuthenticationResponse = new JWTAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);
        return jwtAuthenticationResponse;
    }

    public void initiatePasswordReset(String email) {
        if(isEmailUnique(email)) {
            throw new EmailNotFoundException("This email does not have an account.");
        }

        String token = jwtService.generateResetToken(email);


        String resetLink = resetPasswordUrl + "?token=" + token;


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khaledmlihan497@gmail.com");
        message.setTo(email);
        message.setSubject("Reset Password");
        message.setText("Click the link to reset your password: " + resetLink);
        mailSender.send(message);
    }

    public void completePasswordReset(String token, String newPassword) {
        // Validate the token
        if (jwtService.isResetTokenExpired(token)) {
            throw new RuntimeException("Token expired");
        }

        String email = jwtService.extractEmailFromResetToken(token);
        if(isEmailUnique(email)) {
            throw new RuntimeException("You do not have an account.");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
        Company company = companyRepository.findByEmail(email).orElse(null);
        if(company != null) {
            company.setPassword(passwordEncoder.encode(newPassword));
            companyRepository.save(company);
        }
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if(admin != null) {
            admin.setPassword(passwordEncoder.encode(newPassword));
            adminRepository.save(admin);
        }

    }

    private boolean isEmailUnique(String email) {
        return !adminRepository.findByEmail(email).isPresent() &&
                !userRepository.findByEmail(email).isPresent() &&
                !companyRepository.findByEmail(email).isPresent();
    }
}
