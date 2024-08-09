package com.example.job_portal_api.dtos;

import com.example.job_portal_api.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String nationality;
    private String profileTitle;
    private List<String> skills;
    private List<String> languages;
    private AddressDto address;
    private List<ExperienceDto> experiences;
    private AttachmentDto cv;
    private AttachmentDto profilePicture;
    private boolean archived;
    private String password;
    private String bio;
    private String githubUrl;
    private String linkedinUrl;
    private List<QualificationDto> qualifications;
    private UserType userType;
}
