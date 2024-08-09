package com.example.job_portal_api.services;

import com.example.job_portal_api.dtos.UserDto;
import com.example.job_portal_api.dtos.UserSearchCriteria;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    Page<UserDto> getAllUsers(UserSearchCriteria criteria, Pageable pageable);

    UserDto getUserById(UUID id);

    UserDto createUser(UserDto user);

    UserDto updateUser(UUID id, UserDto userDto);

    void deleteUser(UUID id);

    User uploadCv(UUID userId, MultipartFile file) throws IOException;

    User uploadProfilePicture(UUID userId, MultipartFile file) throws IOException;

    List<Job> getAppliedJobsForUser(UUID userId);

    UserDto applyToJob(UUID userId, UUID jobId);


}
