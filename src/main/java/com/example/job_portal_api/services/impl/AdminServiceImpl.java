package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.AdminDTO;
import com.example.job_portal_api.mappers.AdminMapper;
import com.example.job_portal_api.repositories.AdminRepository;
import com.example.job_portal_api.services.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminMapper.mapToDTOs(adminRepository.findAll());
    }

    @Override
    public AdminDTO getAdminById(UUID id) {
        return adminMapper.mapToDTO(adminRepository.findById(id).orElse(null));
    }

    @Override
    public void deleteAdmin(UUID id) {
        adminRepository.deleteById(id);
    }
}
