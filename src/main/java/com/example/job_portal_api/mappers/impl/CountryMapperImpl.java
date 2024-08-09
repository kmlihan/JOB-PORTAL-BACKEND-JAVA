package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.CountryDto;
import com.example.job_portal_api.entities.Country;
import com.example.job_portal_api.mappers.CountryMapper;
import org.springframework.stereotype.Component;

@Component
public class CountryMapperImpl extends BaseMapperImpl<Country, CountryDto> implements CountryMapper {

    @Override
    public CountryDto mapToDTO(Country country) {
        if (country == null) {
            return null;
        }
        CountryDto dto = new CountryDto();
        dto.setId(country.getId());
        dto.setIso(country.getIso());
        dto.setIso3(country.getIso3());
        dto.setName(country.getName());
        dto.setNiceName(country.getNiceName());
        dto.setNisCode(country.getNisCode());
        dto.setNumcode(country.getNumcode());
        dto.setPhonecode(country.getPhonecode());
        return dto;
    }

    @Override
    public Country mapToEntity(CountryDto dto, Country country) {
        if (dto == null) {
            return null;
        }

        country.setId(dto.getId());


        if (dto.getIso() != null) {
            country.setIso(dto.getIso());
        }
        if (dto.getIso3() != null) {
            country.setIso3(dto.getIso3());
        }
        if (dto.getName() != null) {
            country.setName(dto.getName());
        }
        if (dto.getNiceName() != null) {
            country.setNiceName(dto.getNiceName());
        }
        if (dto.getNisCode() != null) {
            country.setNisCode(dto.getNisCode());
        }
        if (dto.getNumcode() != null) {
            country.setNumcode(dto.getNumcode());
        }
        if (dto.getPhonecode() != null) {
            country.setPhonecode(dto.getPhonecode());
        }

        return country;
    }
}
