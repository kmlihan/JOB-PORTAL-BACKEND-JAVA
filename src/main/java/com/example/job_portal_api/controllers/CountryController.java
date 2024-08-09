package com.example.job_portal_api.controllers;


import com.example.job_portal_api.dtos.CountryDto;
import com.example.job_portal_api.entities.Country;
import com.example.job_portal_api.mappers.CountryMapper;
import com.example.job_portal_api.repositories.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@Tag(description = "CountryService", name = "CountryService")
@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {
    private static final Logger log = LoggerFactory.getLogger(CountryController.class);


    @NonNull
    private final CountryRepository countryRepository;
    @NonNull

    private final CountryMapper countryEntityToDtoMapper;

    @Operation(summary = "getListOfCountries", description = "Get list of all countries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CountryDto.class))), description = "Successfully fetched a list of countries"),
    })
    @GetMapping(value = "",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<List<CountryDto>> getCountriesList() {
        log.info("Get list of countries");
        List<Country> countriesList = countryRepository.findAll();
        List<CountryDto> countryDtoList = countriesList.stream().map(file -> countryEntityToDtoMapper.mapToDTO(file)).collect(Collectors.toList());
        return ok(countryDtoList);
    }

    @Operation(summary = "getCountry", description = "Get country")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = CountryDto.class)), description = "Successfully fetched a country"),
    })
    @GetMapping(value = "{countryIso}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    ResponseEntity<CountryDto> getCountry(@PathVariable String countryIso) {
        log.info("Get a country");
        Country country = countryRepository.findCountryByIso(countryIso);
        if (country != null) {
            CountryDto countryDto = countryEntityToDtoMapper.mapToDTO(country);
            return ok(countryDto);
        }
        return notFound().build();
    }
}
