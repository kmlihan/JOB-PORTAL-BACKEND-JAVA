package com.example.job_portal_api.dtos.swagger;

import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.dtos.JobListDto;
import org.springframework.data.domain.Page;

public interface PageOfJobListDto extends Page<JobListDto> {
}
