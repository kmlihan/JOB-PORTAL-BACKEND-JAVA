package com.example.job_portal_api.dtos;

import com.example.job_portal_api.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private UserType userType;
}
