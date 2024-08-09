package com.example.job_portal_api.services;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.UUID;

public interface JWTService {
    String generateToken(UserDetails userDetails, String role, String id);

    String extractUserName(String token);

    boolean isTokenValid(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails);
    String generateResetToken(String email);
    String extractEmailFromResetToken(String token);
    boolean isResetTokenExpired(String token);
}
