package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.dtos.JobSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

public interface JobService {

    JobDto createJob(JobDto jobDto);

    JobDto updateJob(UUID jobId, JobDto jobDto);

    JobDto getJobById(UUID jobId);

    Page<JobDto> getAllJobs(JobSearchCriteria criteria, Pageable pageable);

    void deleteJob(UUID jobId);

    void sendInterviewLink(UUID jobId, String userEmail);
    ByteArrayInputStream generateJobPdf(UUID jobId) throws IOException;
}
