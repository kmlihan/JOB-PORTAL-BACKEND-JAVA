package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.AdminDTO;
import com.example.job_portal_api.dtos.SimpleStatusDTO;
import com.example.job_portal_api.services.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Tag(description = "AdminService", name = "AdminService")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get all admins",
            description = "Get all admins.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOt found any matching result.",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),

                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully listed admins.",
                            content = @Content(schema = @Schema(implementation = List.class))
                    )

            })
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable UUID id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }
}
