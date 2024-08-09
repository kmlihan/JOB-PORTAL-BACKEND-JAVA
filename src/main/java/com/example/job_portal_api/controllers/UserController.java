package com.example.job_portal_api.controllers;

import com.example.job_portal_api.dtos.*;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.entities.User;
import com.example.job_portal_api.mappers.JobMapper;
import com.example.job_portal_api.repositories.UserRepository;
import com.example.job_portal_api.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(description = "UserService", name = "UserService")

public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobMapper jobMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Get all users",
            description = "List all users.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully listed users",
                            content = @Content(schema = @Schema(implementation = Page.class))
                    )
            }
    )
    public @ResponseBody ResponseEntity<Page<UserDto>> getAllUsers(@RequestBody UserSearchCriteria criteria,  Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(criteria, pageable));
    }

    @GetMapping(path="/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by id",
            description = "Get user by id.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get user",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            }
    )
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        UserDto user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }


    @PutMapping(path="/{id}")
    @Operation(summary = "Update user by id",
            description = "Update user by id.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully update user",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            }
    )
    public ResponseEntity<?> updateUser(@PathVariable UUID id,
                                        @RequestBody UserDto userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "Not found any user with id : " + id,
                    404,
                    null)
            );
        }
        UserDto updatedUser = userService.updateUser(id, userDetails);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path="/{id}")
    @Operation(summary = "Delete user by id",
            description = "Delete user by id.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully delete user",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body(new SimpleStatusDTO(
                    "Not found any user with id : " + id,
                    404,
                    null)
            );
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{userId}/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Upload cv to  user",
            description = "Upload cv to  user.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully uploaded cv to user",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> uploadCV(@PathVariable UUID userId, @RequestParam("file") MultipartFile cv) {
        try {
            userService.uploadCv(userId, cv);
            return ResponseEntity.ok(new SimpleStatusDTO("CV uploaded successfully", 200, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload CV");
        }
    }

    @PostMapping(value = "/{userId}/profilePicture", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload profile picture to  user",
            description = "Upload profile picture to  user.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully uploaded profile picture to user",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    )
            }
    )
    public ResponseEntity<?> uploadProfilePicture(@PathVariable UUID userId, @RequestParam("file") MultipartFile profilePicture) {
        try {
            userService.uploadProfilePicture(userId, profilePicture);
            return ResponseEntity.ok(new SimpleStatusDTO("Profile picture uploaded successfully", 200, null));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    @GetMapping(value = "/{userId}/applied-jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get applied jobs for user",
            description = "Get applied jobs for user.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully get applied jobs for user",
                            content = @Content(array = @ArraySchema(schema = @Schema(implementation = JobDto.class)))
                    )
            }
    )
    public ResponseEntity<List<JobDto>> getAppliedJobsForUser(@PathVariable UUID userId) {
        List<Job> appliedJobs = userService.getAppliedJobsForUser(userId);
        List<JobDto> dtos = jobMapper.mapToDTOs(appliedJobs);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping(value = "/apply/{userId}/{jobId}")
    @Operation(summary = "Apply to job",
            description = "Apply to job.",
            responses = {
                    @ApiResponse(responseCode = "404",
                            description = "Not found any matching results",
                            content = @Content(schema = @Schema(implementation = SimpleStatusDTO.class))
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Successfully  applied to job",
                            content = @Content(schema = @Schema(implementation = UserDto.class))
                    )
            }
    )
    public ResponseEntity<?> applyToJob(@PathVariable UUID userId, @PathVariable UUID jobId) {
        return ResponseEntity.ok(userService.applyToJob(userId, jobId));
    }
}
