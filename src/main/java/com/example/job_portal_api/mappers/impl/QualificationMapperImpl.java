package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.QualificationDto;
import com.example.job_portal_api.entities.Qualification;
import com.example.job_portal_api.mappers.QualificationMapper;
import org.springframework.stereotype.Component;

@Component
public class QualificationMapperImpl extends BaseMapperImpl<Qualification, QualificationDto> implements QualificationMapper {
    @Override
    public QualificationDto mapToDTO(Qualification qualification) {
        QualificationDto dto = new QualificationDto();
        dto.setId(qualification.getId());
        dto.setDegree(qualification.getDegree());
        dto.setStartDate(qualification.getStartDate());
        dto.setEndDate(qualification.getEndDate());
        return dto;
    }

    @Override
    public Qualification mapToEntity(QualificationDto dto, Qualification qualification) {
        if (dto.getDegree() != null) {
            qualification.setDegree(dto.getDegree());
        }
        if (dto.getStartDate() != null) {
            qualification.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            qualification.setEndDate(dto.getEndDate());
        }
        return qualification;
    }

}
