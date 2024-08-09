package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.AdminDTO;
import com.example.job_portal_api.entities.Admin;
import com.example.job_portal_api.mappers.AdminMapper;
import org.springframework.stereotype.Component;

@Component
public class AdminMapperImpl extends BaseMapperImpl<Admin, AdminDTO> implements AdminMapper {
    @Override
    public AdminDTO mapToDTO(Admin admin) {
        AdminDTO dto = new AdminDTO();
        dto.setId(admin.getId());
        dto.setFirstName(admin.getFirstName());
        dto.setLastName(admin.getLastName());
        dto.setEmail(admin.getEmail());
        dto.setUserType(admin.getUserType());
        return dto;
    }

    @Override
    public Admin mapToEntity(AdminDTO dto, Admin admin) {
        if (dto.getFirstName() != null) {
            admin.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            admin.setLastName(dto.getLastName());
        }
        if (dto.getEmail() != null) {
            admin.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            admin.setPassword(dto.getPassword());
        }
        if(dto.getUserType() != null) {
            admin.setUserType(dto.getUserType());
        }
        return admin;
    }

}
