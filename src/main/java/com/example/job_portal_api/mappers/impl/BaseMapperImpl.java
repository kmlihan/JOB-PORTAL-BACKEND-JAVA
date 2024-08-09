package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.mappers.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseMapperImpl<ENTITY, DTO> implements BaseMapper<ENTITY, DTO> {
    @Override
    public Page<DTO> mapPageToDTOs(Page<ENTITY> entities) {
        return mapPageToCustomDTOPage(entities, this::mapToDTO);
    }

    protected <DTOTYPE> Page<DTOTYPE> mapPageToCustomDTOPage(Page<ENTITY> entities, Function<? super ENTITY, ? extends DTOTYPE> mapper) {
        final Long totalCount = entities.getTotalElements();
        final Pageable pageable = entities.getPageable();

        List<DTOTYPE> resultList = entities.getContent().stream().map(mapper).collect(Collectors.toList());
        return new PageImpl<>(resultList, pageable, totalCount);

    }

    @Override
    public List<DTO> mapToDTOs(List<ENTITY> content) {
        return content.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Set<DTO> mapToDTOs(Set<ENTITY> content) {
        return content.stream().map(this::mapToDTO).collect(Collectors.toSet());
    }
}
