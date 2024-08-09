package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.ContactRequest;
import com.example.job_portal_api.dtos.SimpleStatusDTO;
import com.example.job_portal_api.services.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(description = "ContactService", name = "ContactService")
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ContactService contactService;

    @PostMapping("/contact")
    @Operation(summary = "Contact Us",
            description = "Send a message through the contact form",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Your message has been sent successfully!",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<SimpleStatusDTO> handleContact(@RequestBody ContactRequest contactRequest) {
        contactService.sendEmail(contactRequest);
        return ResponseEntity.ok(new SimpleStatusDTO(
                "Your message has been sent successfully!",
                200,
                null)
        );
    }


}






