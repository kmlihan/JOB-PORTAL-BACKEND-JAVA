package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.repositories.AttachmentRepository;
import com.example.job_portal_api.services.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public Attachment saveAttachment(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (fileName.contains("..")) {
                throw new Exception("Filename contains invalid path sequence " + fileName);
            }
            Attachment attachment = new Attachment();
            attachment.setFileName(fileName);
            attachment.setFileType(file.getContentType());
            attachment.setData(file.getBytes());

            return attachmentRepository.save(attachment);

        } catch (Exception e) {
            throw new Exception("Could not save File:  " + fileName);
        }

    }

    @Override
    public Attachment getAttachment(String fileId) throws Exception {
        return attachmentRepository.findById(UUID.fromString(fileId)).orElseThrow(() -> new Exception("File not found "));
    }
}
