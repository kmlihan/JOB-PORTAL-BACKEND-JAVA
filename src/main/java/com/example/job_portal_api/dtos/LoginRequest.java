package com.example.job_portal_api.dtos;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
