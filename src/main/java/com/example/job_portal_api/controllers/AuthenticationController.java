package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.*;
import com.example.job_portal_api.exceptions.EmailAlreadyUsedException;
import com.example.job_portal_api.repositories.AdminRepository;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.UserRepository;
import com.example.job_portal_api.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path="/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(description = "AuthService", name = "AuthService")
public class AuthenticationController {


    private final AuthenticationService authenticationService;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/login")
    @Operation(summary = "login",
            description = "login",
            responses = {

                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Login successfully",
                            content = @Content(schema = @Schema(implementation = JWTAuthenticationResponse.class))
                    )
            }
    )
    public ResponseEntity<JWTAuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping(value = "/register/admin")
    @Operation(summary = "Register admin",
            description = "Register admin",
            responses = {

                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Register admin successfully",
                            content = @Content(schema = @Schema(implementation = AdminDTO.class))
                    )
            }
    )
    public ResponseEntity<?> registerAdmin(@RequestBody AdminDTO admin) {
        if (!isEmailUnique(admin.getEmail())) {
            throw new EmailAlreadyUsedException("Email already used");
        }
        return ResponseEntity.ok(authenticationService.registerAdmin(admin));
    }

    @PostMapping(value = "/register/company")
    @Operation(summary = "Register company",
            description = "Register company",
            responses = {

                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Register company successfully",
                            content = @Content(schema = @Schema(implementation = CompanyDto.class))
                    )
            }
    )
    public @ResponseBody ResponseEntity registerCompany(@RequestBody CompanyDto company) throws IOException {
        if (!isEmailUnique(company.getEmail())) {
            throw new EmailAlreadyUsedException("Email already used");
        }
        try {

            return ResponseEntity.ok(authenticationService.registerCompany(company));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Server error");
        }
    }

    @PostMapping(value = "/register/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register user",
            description = "Register user",
            responses = {

                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Register user successfully",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            }
    )
    public ResponseEntity<?> registerUser(@RequestBody UserDto user) {
        if (!isEmailUnique(user.getEmail())) {
            throw new EmailAlreadyUsedException("Email already used");
        }
        return ResponseEntity.ok(authenticationService.registerUser(user));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password",
            description = "Initiate password reset process",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Password reset link has been sent to your email",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        authenticationService.initiatePasswordReset(email);
             return ResponseEntity.status(200).body(new SimpleStatusDTO(
                "Password reset link has been sent to your email",
                200,
                null)
        );
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password",
            description = "Complete password reset process",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Password has been reset successfully",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        authenticationService.completePasswordReset(token, newPassword);
        return ResponseEntity.status(200).body(new SimpleStatusDTO(
                "Password has been reset successfully",
                200,
                null)
        );

    }
    private boolean isEmailUnique(String email) {
        return !adminRepository.findByEmail(email).isPresent() &&
                !userRepository.findByEmail(email).isPresent() &&
                !companyRepository.findByEmail(email).isPresent();
    }

}
