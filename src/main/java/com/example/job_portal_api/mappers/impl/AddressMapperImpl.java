package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.AddressDto;
import com.example.job_portal_api.dtos.CountryDto;
import com.example.job_portal_api.entities.Address;
import com.example.job_portal_api.entities.Country;
import com.example.job_portal_api.mappers.AddressMapper;
import com.example.job_portal_api.mappers.CountryMapper;
import com.example.job_portal_api.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AddressMapperImpl extends BaseMapperImpl<Address, AddressDto> implements AddressMapper {

    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private  CountryRepository countryRepository;

    @Override
    public AddressDto mapToDTO(Address address) {
        if (address == null) {
            return null;
        }
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setHouseNumber(address.getHouseNumber());
        dto.setBox(address.getBox());
        dto.setCity(address.getCity());
        dto.setPostalCode(address.getPostalCode());
        Country country = address.getCountry();
        System.out.println("Country: " + country);

        if (country != null) {
            CountryDto countryDto = countryMapper.mapToDTO(country);
            System.out.println("Mapped Country DTO: " + countryDto);

            if (countryDto != null) {
                dto.setCountry(countryDto.getIso());
            }
        }
//        dto.setCountry(Optional.ofNullable(address.getCountry()).map(x -> countryMapper.mapToDTO(x)).map(x-> x.getIso()).orElse(null));
        dto.setCreatedDate(address.getCreatedDate());
        dto.setLastModifiedDate(address.getLastModifiedDate());
        return dto;
    }

    @Override
    public Address mapToEntity(AddressDto dto, Address address) {
        if (dto == null) {
            return null;
        }
        if(address == null) {
            address = new Address();
        }
        // Update fields if provided
        if (dto.getStreet() != null) {
            address.setStreet(dto.getStreet());
        }
        if (dto.getHouseNumber() != null) {
            address.setHouseNumber(dto.getHouseNumber());
        }
        if (dto.getBox() != null) {
            address.setBox(dto.getBox());
        }
        if (dto.getCity() != null) {
            address.setCity(dto.getCity());
        }
        if (dto.getPostalCode() != null) {
            address.setPostalCode(dto.getPostalCode());
        }
        if (dto.getCountry() != null) {
            if(address.getCountry() == null) {
                address.setCountry(new Country());
            }
            address.setCountry(Optional.ofNullable(dto.getCountry()).map(countryRepository::findCountryByIso).orElse(null));
        }
        if (dto.getCreatedDate() != null) {
            address.setCreatedDate(dto.getCreatedDate());
        }
        if (dto.getLastModifiedDate() != null) {
            address.setLastModifiedDate(dto.getLastModifiedDate());
        }

        return address;
    }
}
