package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.AttachmentDto;
import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.mappers.AttachmentMapper;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapperImpl extends BaseMapperImpl<Attachment, AttachmentDto> implements AttachmentMapper {

    @Override
    public AttachmentDto mapToDTO(Attachment attachment) {
        if (attachment == null) {
            return null;
        }
        AttachmentDto dto = new AttachmentDto();
        dto.setId(attachment.getId());
        dto.setFileType(attachment.getFileType());
        dto.setFileName(attachment.getFileName());
        dto.setData(attachment.getData());
        return dto;
    }

    @Override
    public Attachment mapToEntity(AttachmentDto dto, Attachment attachment) {
        if (dto == null) {
            return null;
        }


        if (dto.getFileType() != null) {
            attachment.setFileType(dto.getFileType());
        }
        if (dto.getFileName() != null) {
            attachment.setFileName(dto.getFileName());
        }
        if (dto.getData() != null) {
            attachment.setData(dto.getData());
        }

        return attachment;
    }
}
