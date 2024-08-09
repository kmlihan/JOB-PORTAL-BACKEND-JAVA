package com.example.job_portal_api.mappers;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface BaseMapper<ENTITY, DTO> {
    DTO mapToDTO(ENTITY entity);

    ENTITY mapToEntity(DTO dto, ENTITY entity);

    Page<DTO> mapPageToDTOs(Page<ENTITY> entities);

    List<DTO> mapToDTOs(List<ENTITY> content);

    Set<DTO> mapToDTOs(Set<ENTITY> content);
}
