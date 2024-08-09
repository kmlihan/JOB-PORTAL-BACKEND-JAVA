package com.example.job_portal_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QualificationDto {
    private UUID id;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;
}
