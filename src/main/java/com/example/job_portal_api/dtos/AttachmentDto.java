package com.example.job_portal_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
    private UUID id;
    private String fileName;
    private String fileType;
    private byte[] data;
}
