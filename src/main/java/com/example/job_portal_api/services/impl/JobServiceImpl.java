package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.JobDto;
import com.example.job_portal_api.dtos.JobSearchCriteria;
import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.entities.Company;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.mappers.JobMapper;
import com.example.job_portal_api.repositories.CompanyRepository;
import com.example.job_portal_api.repositories.JobRepository;
import com.example.job_portal_api.repositories.specifications.JobSpecifications;
import com.example.job_portal_api.services.AccessService;
import com.example.job_portal_api.services.JobService;
import com.example.job_portal_api.utils.LogoUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final JavaMailSender mailSender;
    private final AccessService accessService;
    private final CompanyRepository companyRepository;

    @Override
    public JobDto createJob(JobDto jobDto) {
        if (!accessService.isAdmin() && !accessService.isCompanyRole()) {
            throw new SecurityException("Access denied");
        }
        Company company = companyRepository.findById(jobDto.getCompanyId()).orElse(null);

        Job job = jobMapper.mapToEntity(jobDto, new Job());
        if (company != null) {
            job.setCompanyName(company.getName());
            String companyLogo = LogoUtil.getLogoUrl(company.getLogo());
            job.setCompanyLogo(companyLogo);
        }
        Job savedJob = jobRepository.save(job);
        return jobMapper.mapToDTO(savedJob);
    }

    @Override
    public JobDto updateJob(UUID jobId, JobDto jobDto) {
        if (!accessService.hasJobAccess(jobId)) {
            throw new SecurityException("Access denied");
        }
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        job = jobMapper.mapToEntity(jobDto, job);
        Job updatedJob = jobRepository.save(job);
        return jobMapper.mapToDTO(updatedJob);
    }

    @Override
    public JobDto getJobById(UUID jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));
        return jobMapper.mapToDTO(job);
    }

    @Override
    public Page<JobDto> getAllJobs(JobSearchCriteria criteria, Pageable pageable) {
        Specification<Job> spec = JobSpecifications.withCriteria(criteria);
        return jobMapper.mapPageToDTOs(jobRepository.findAll(spec, pageable));
    }

    @Override
    public void deleteJob(UUID jobId) {
        if (!accessService.hasJobAccess(jobId)) {
            throw new SecurityException("Access denied");
        }
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));
        jobRepository.delete(job);
    }

    @Override
    public void sendInterviewLink(UUID jobId, String userEmail) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new EntityNotFoundException("Job not found with ID: " + jobId));

        String interviewLink = "https://example.com/interview/" + jobId;

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("your-sender@example.com");
            helper.setTo(userEmail);
            helper.setSubject("Interview Invitation");
            helper.setText("You are invited to an interview. Please use the following link to join the interview: " + interviewLink);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Handle the exception as needed
            throw new RuntimeException("Failed to send email", e);
        }
    }

    public ByteArrayInputStream generateJobPdf(UUID jobId) throws IOException {
        Optional<Job> jobOptional = jobRepository.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();

            try (PDDocument document = PDDocument.load(getClass().getResourceAsStream("/templates/job_template.pdf"))) {
                PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
                int experience = (int) job.getExperience();
                String year = experience > 1 ? "years" : "year";
                List<String> benefits = job.getBenefits();
                String formattedBenefits = benefits.stream()
                        .map(benefit -> "- " + benefit)
                        .collect(Collectors.joining("\n "));
                List<String> responsibilities = job.getResponsibilities();
                String formattedResponsibilities = responsibilities.stream()
                        .map(responsibility -> "- " + responsibility)
                        .collect(Collectors.joining("\n "));

                if (acroForm != null) {
                    fillField(acroForm, "jobTitle", job.getTitle());
                    fillField(acroForm, "description", job.getDescription());
                    fillField(acroForm, "experience", "" + experience + " " + year);
                    fillField(acroForm, "salary", Integer.toString((int) job.getSalary()));
                    fillField(acroForm, "contractType", job.getContractType().toString());
                    fillField(acroForm, "jobType", job.getJobType().toString());
                    fillField(acroForm, "levelOfEducation", job.getLevelOfEducation().toString());
                    fillField(acroForm, "address", job.getAddress().toString());
                    fillField(acroForm, "skills", String.join(", ", job.getSkills()));
                    fillField(acroForm, "responsibilities", formattedResponsibilities);
                    fillField(acroForm, "benefits", formattedBenefits);
                    fillField(acroForm, "languages", String.join(", ", job.getLanguages()));

                    // Retrieve the logo from the company
                    Attachment logoAttachment = job.getCompany().getLogo();
                    if (logoAttachment != null) {
                        // Find the logo field in the PDF form
                        PDField logoField = acroForm.getField("logoField");
                        if (logoField != null) {
                            // Get the position and size of the logo field
                            PDAnnotationWidget widget = logoField.getWidgets().get(0);
                            PDRectangle rect = widget.getRectangle();
                            float x = rect.getLowerLeftX();
                            float y = rect.getUpperRightY();
                            float width = rect.getWidth();
                            float height = rect.getHeight();
                            logoField.setReadOnly(true);
                            // Add the logo to the PDF
                            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, logoAttachment.getData(), logoAttachment.getFileName());
                            PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(0), PDPageContentStream.AppendMode.APPEND, true, true);

                            contentStream.drawImage(pdImage, x, y, width, height);
                            contentStream.close();
                        }
                    }

                    // Save the filled form to a byte array
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    acroForm.flatten();
                    document.save(out);

                    return new ByteArrayInputStream(out.toByteArray());
                } else {
                    throw new IllegalArgumentException("AcroForm not found in the template");
                }
            }
        } else {
            throw new IllegalArgumentException("Job not found with ID: " + jobId);
        }
    }

    private void fillField(PDAcroForm acroForm, String fieldName, String value) throws IOException {
        PDField field = acroForm.getField(fieldName);
        if (field != null) {
            field.setValue(value);
            field.setReadOnly(true);
        }
    }
}
