package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.dtos.JobSearchCriteria;
import com.example.job_portal_api.dtos.SimpleStatusDTO;
import com.example.job_portal_api.dtos.swagger.PageOfJobListDto;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.repositories.JobRepository;
import com.example.job_portal_api.services.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("api/jobs")
@Tag(description = "JobService", name = "JobService")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;


    @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create job ",
            description = "Create job.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully created job by id",
                            content = @Content(schema = @Schema(implementation = JobDto.class))
                    )
            }
    )
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        JobDto createdJob = jobService.createJob(jobDto);
        return ResponseEntity.ok(createdJob);
    }

    @PutMapping(value = "/{jobId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update job ",
            description = "Update job.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully updated job by id",
                            content = @Content(schema = @Schema(implementation = JobDto.class))
                    )
            }
    )
    public ResponseEntity<?> updateJob(@PathVariable UUID jobId, @RequestBody JobDto jobDto) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "Not found any job with id : " + jobId,
                    404,
                    null)
            );
        }
        JobDto updatedJob = jobService.updateJob(jobId, jobDto);
        return ResponseEntity.ok(updatedJob);
    }

    @GetMapping(value = "/{jobId}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get job by id",
            description = "Get job by id.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get job by id",
                            content = @Content(schema = @Schema(implementation = JobDto.class))
                    )
            }
    )
    public ResponseEntity<?> getJobById(@PathVariable UUID jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "Not found any job with id : " + jobId,
                    404,
                    null)
            );
        }
        JobDto jobDto = jobService.getJobById(jobId);
        return ResponseEntity.ok(jobDto);
    }

    @PutMapping(value = "/",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all jobs",
            description = "List all jobs based on search criteria.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully listed jobs",
                            content = @Content(schema = @Schema(implementation = PageOfJobListDto.class))
                    )
            }
    )
    @PageableAsQueryParam
    public ResponseEntity<Page<JobDto>> getAllJobs(
            @RequestBody JobSearchCriteria criteria,
            @Parameter(hidden = true) Pageable pageable
    ) {
        Page<JobDto> jobs = jobService.getAllJobs(criteria, pageable);
        return ResponseEntity.ok(jobs);
    }

    @DeleteMapping(value = "/{jobId}")
    @Operation(summary = "Delete job",
            description = "Delete job .",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully deleted job by id",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> deleteJob(@PathVariable UUID jobId) {
        Job job = jobRepository.findById(jobId).orElse(null);
        if (job == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "Job with id: " + jobId + "  does not exists.",
                    404,
                    null)
            );
        }
        jobService.deleteJob(jobId);
        return ResponseEntity.status(200).body(new SimpleStatusDTO(
                "Job with id: " + jobId + " is deleted.",
                200,
                null)
        );
    }

    @PostMapping(value = "/{jobId}/{userEmail}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Send interview link",
            description = "Send an interview link to the specified user for the given job.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Job not found",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Access denied",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Interview link sent successfully",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<SimpleStatusDTO> sendInterviewLink(
            @PathVariable UUID jobId,
            @PathVariable("userEmail") String userEmail
    ) {
        jobService.sendInterviewLink(jobId, userEmail);
        return ResponseEntity.ok(new SimpleStatusDTO("Interview link sent successfully", 200, null));
    }

    @GetMapping("/{jobId}/pdf")
    public ResponseEntity<InputStreamResource> downloadJobPdf(@PathVariable UUID jobId) {
        try {
            ByteArrayInputStream pdfStream = jobService.generateJobPdf(jobId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=job_" + jobId + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(pdfStream));
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}

