package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.CompanyDto;
import com.example.job_portal_api.dtos.CompanySearchCriteria;
import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.dtos.SimpleStatusDTO;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.mappers.JobMapper;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.services.CompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/companies")
@Tag(description = "CompanyService", name = "CompanyService")
public class CompanyController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JobMapper jobMapper;

    @GetMapping(path="/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all companies",
            description = "Retrieve all companies as a list",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved companies",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CompanyDto.class)))
                    )
            }
    )
    public ResponseEntity<List<CompanyDto>> getAllCompaniesAsList() {
        List<CompanyDto> companies = companyService.getAllCompaniesList();
        return ResponseEntity.ok(companies);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all companies",
            description = "List all comapnies.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully listed companies",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    public @ResponseBody ResponseEntity<Page<CompanyDto>> getAllCompanies(
            @RequestBody CompanySearchCriteria criteria,
             Pageable pageable
    ) {
        Page<CompanyDto> companies = companyService.getAllCompanies(criteria, pageable);
        return ResponseEntity.ok(companies);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get company by id",
            description = "Get company by id.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get company by id",
                            content = @Content(schema = @Schema(implementation = CompanyDto.class))
                    )
            }
    )
    public ResponseEntity<?> getCompanyById(@PathVariable UUID id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "company with id " + id + " not found",
                    404,
                    null

            ));
        }
        CompanyDto companyDto = companyService.getCompanyById(id);
        return ResponseEntity.ok(companyDto);
    }


    @PutMapping(value = "/{id}")
    @Operation(summary = "Update company ",
            description = "Update company .",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated company",
                            content = @Content(schema = @Schema(implementation = CompanyDto.class))
                    )
            }
    )
    public ResponseEntity<?> updateCompany(@PathVariable UUID id, @RequestBody CompanyDto companyDto)  {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "company with id " + id + " not found",
                    404,
                    null

            ));
        }

        CompanyDto updatedCompany = companyService.updateCompany(id, companyDto);
        if (updatedCompany != null) {
            return ResponseEntity.ok(updatedCompany);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete company ",
            description = "Delete company .",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully deleted company",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> deleteCompany(@PathVariable UUID id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "company with id " + id + " not found",
                    404,
                    null

            ));
        }
        companyService.deleteCompany(id);
        return ResponseEntity.status(200).body(new SimpleStatusDTO(
                "company with id: " + id + " is deleted.",
                200,
                null)
        );
    }

    @PostMapping(value = "/{companyId}/upload-logo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload company logo",
            description = "Upload a logo for the specified company.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Company not found",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Logo uploaded successfully",
                            content = @Content(schema = @Schema(implementation = CompanyDto.class))
                    )
            }
    )
    public ResponseEntity<?> uploadLogo(
            @PathVariable UUID companyId,
            @RequestParam("logo") MultipartFile logo
    ) throws IOException {
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "company with id " + companyId + " not found",
                    404,
                    null

            ));
        }
        CompanyDto companyDto = companyService.uploadLogo(companyId, logo);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping(value = "/{companyId}/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all jobs for company",
            description = "Get all jobs for company.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get applied jobs for user",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = JobDto.class)))
                    )
            }
    )
    public ResponseEntity<List<JobDto>> getAllJobsForCompany(@PathVariable UUID companyId) {
        List<Job> jobs = companyService.getJobsCompany(companyId);
        List<JobDto> dtos = jobMapper.mapToDTOs(jobs);
        return ResponseEntity.ok(dtos);
    }
}
