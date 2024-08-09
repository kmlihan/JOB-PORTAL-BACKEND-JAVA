package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.ExperienceDto;
import com.example.job_portal_api.entities.Experience;
import com.example.job_portal_api.mappers.ExperienceMapper;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapperImpl extends BaseMapperImpl<Experience, ExperienceDto> implements ExperienceMapper {

    @Override
    public ExperienceDto mapToDTO(Experience experience) {
        if (experience == null) {
            return null;
        }
        ExperienceDto dto = new ExperienceDto();
        dto.setId(experience.getId());
        dto.setCompanyName(experience.getCompanyName());
        dto.setStartDate(experience.getStartDate());
        dto.setEndDate(experience.getEndDate());
        dto.setJobTitle(experience.getJobTitle());
        dto.setRoleDescription(experience.getRoleDescription());
        return dto;
    }

    @Override
    public Experience mapToEntity(ExperienceDto dto, Experience experience) {
        if (dto == null) {
            return null;
        }

        if (dto.getCompanyName() != null) {
            experience.setCompanyName(dto.getCompanyName());
        }
        if (dto.getStartDate() != null) {
            experience.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            experience.setEndDate(dto.getEndDate());
        }
        if (dto.getJobTitle() != null) {
            experience.setJobTitle(dto.getJobTitle());
        }
        if (dto.getRoleDescription() != null) {
            experience.setRoleDescription(dto.getRoleDescription());
        }

        return experience;
    }
}
