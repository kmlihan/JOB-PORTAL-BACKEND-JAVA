package com.example.job_portal_api.services.impl;

import com.example.job_portal_api.dtos.UserDto;
import com.example.job_portal_api.dtos.UserSearchCriteria;
import com.example.job_portal_api.entities.Attachment;
import com.example.job_portal_api.entities.Job;
import com.example.job_portal_api.entities.User;
import com.example.job_portal_api.mappers.UserMapper;
import com.example.job_portal_api.repositories.JobRepository;
import com.example.job_portal_api.repositories.UserRepository;
import com.example.job_portal_api.repositories.specifications.UserSpecifications;
import com.example.job_portal_api.services.AccessService;
import com.example.job_portal_api.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private AccessService accessService;

    @Override
    public Page<UserDto> getAllUsers(UserSearchCriteria criteria, Pageable pageable) {
        if (!accessService.isAdmin()) {
            throw new SecurityException("Access denied");
        }
        Specification<User> specification = UserSpecifications.withCriteria(criteria);
        return userMapper.mapPageToDTOs(userRepository.findAll(specification, pageable));
    }

    @Override
    public UserDto getUserById(UUID id) {
        return userMapper.mapToDTO(userRepository.findById(id).orElse(null));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        userMapper.mapToEntity(userDto, user);
        userRepository.save(user);
        return userMapper.mapToDTO(user);
    }

    @Override
    public UserDto updateUser(UUID id, UserDto userDto) {
        if (!accessService.hasUserAccess(id)) {
            throw new SecurityException("Access denied");
        }

        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            return null;
        }

        userMapper.mapToEntity(userDto, existingUser);
        userRepository.save(existingUser);
        return userMapper.mapToDTO(existingUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!accessService.hasUserAccess(id)) {
            throw new SecurityException("Access denied");
        }
        userRepository.deleteById(id);
    }

    public User uploadCv(UUID userId, MultipartFile cv) throws IOException {
//        if (!accessService.hasUserAccess(userId)) {
//            throw new SecurityException("Access denied");
//        }
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Attachment cvAttachment = new Attachment();
        cvAttachment.setFileName(cv.getOriginalFilename());
        cvAttachment.setFileType(cv.getContentType());
        cvAttachment.setData(cv.getBytes());

        user.setCv(cvAttachment);

        return userRepository.save(user);
    }

    public User uploadProfilePicture(UUID userId, MultipartFile profilePicture) throws IOException {
//        if (!accessService.hasUserAccess(userId)) {
//            throw new SecurityException("Access denied");
//        }
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Attachment profilePictureAttachment = new Attachment();
        profilePictureAttachment.setFileName(profilePicture.getOriginalFilename());
        profilePictureAttachment.setFileType(profilePicture.getContentType());
        profilePictureAttachment.setData(profilePicture.getBytes());

        user.setProfilePicture(profilePictureAttachment);

        return userRepository.save(user);
    }

    public List<Job> getAppliedJobsForUser(UUID userId) {
        if (!accessService.hasUserAccess(userId)) {
            throw new SecurityException("Access denied");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getAppliedJobs();
    }

    public UserDto applyToJob(UUID userId, UUID jobId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));
       boolean alreadyApplied = user.getAppliedJobs().contains(job);
        if (alreadyApplied) {
            return userMapper.mapToDTO(user);
        } else {
            user.getAppliedJobs().add(job);
            job.getApplicants().add(user);

            userRepository.save(user);
            jobRepository.save(job);

            return userMapper.mapToDTO(user);
        }
    }



}
