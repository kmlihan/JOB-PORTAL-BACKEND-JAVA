package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.AdminDTO;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    List<AdminDTO> getAllAdmins();

    AdminDTO getAdminById(UUID id);

    void deleteAdmin(UUID id);
}
