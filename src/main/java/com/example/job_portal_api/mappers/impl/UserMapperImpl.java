package com.example.job_portal_api.mappers.impl;

import com.example.job_portal_api.dtos.ExperienceDto;
import com.example.job_portal_api.dtos.QualificationDto;
import com.example.job_portal_api.dtos.UserDto;
import com.example.job_portal_api.entities.*;
import com.example.job_portal_api.mappers.*;
import com.example.job_portal_api.repositories.ExperienceRepository;
import com.example.job_portal_api.repositories.QualificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl extends BaseMapperImpl<User, UserDto> implements UserMapper {
    @Autowired
    private AddressMapper addressMapper;
    @Autowired
    private ExperienceMapper experienceMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private QualificationRepository qualificationRepository;
    @Autowired
    private QualificationMapper qualificationMapper;

    @Override
    public UserDto mapToDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setNationality(user.getNationality());
        userDto.setProfileTitle(user.getProfileTitle());
        userDto.setSkills(user.getSkills());
        userDto.setLanguages(user.getLanguages());
        userDto.setAddress(addressMapper.mapToDTO(user.getAddress()));
        userDto.setExperiences(experienceMapper.mapToDTOs(user.getExperiences()));
        userDto.setCv(attachmentMapper.mapToDTO(user.getCv()));
        userDto.setProfilePicture(attachmentMapper.mapToDTO(user.getProfilePicture()));
        userDto.setArchived(user.isArchived());
        userDto.setPassword(user.getPassword());
        userDto.setBio(user.getBio());
        userDto.setGithubUrl(user.getGithubUrl());
        userDto.setLinkedinUrl(user.getLinkedinUrl());
        userDto.setQualifications(qualificationMapper.mapToDTOs(user.getQualifications()));
        userDto.setUserType(user.getUserType());
        return userDto;
    }


    @Override
    public User mapToEntity(UserDto userDto, User user) {
        if (userDto == null) {
            return null;
        }

        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPhone() != null) {
            user.setPhone(userDto.getPhone());
        }
        if (userDto.getBirthDate() != null) {
            user.setBirthDate(userDto.getBirthDate());
        }
        if (userDto.getNationality() != null) {
            user.setNationality(userDto.getNationality());
        }
        if (userDto.getProfileTitle() != null) {
            user.setProfileTitle(userDto.getProfileTitle());
        }

        if (userDto.getSkills() != null) {
            user.getSkills().clear();
            user.setSkills(userDto.getSkills());
        }

        if (userDto.getLanguages() != null) {
            user.getLanguages().clear();
            user.setLanguages(userDto.getLanguages());
        }

        if (userDto.getAddress() != null) {
            user.setAddress(addressMapper.mapToEntity(userDto.getAddress(), new Address()));
        }

        if (userDto.getExperiences() != null) {
            user.getExperiences().clear();
            for (ExperienceDto experienceDto : userDto.getExperiences()) {
                Experience experience;
                if (experienceDto.getId() != null) {
                    experience = experienceRepository.findById(experienceDto.getId()).orElse(new Experience());
                } else {
                    experience = new Experience();
                }
                experience = experienceMapper.mapToEntity(experienceDto, experience);
                experience.setUser(user);  // Set the user reference
                user.getExperiences().add(experience);
            }
        }

        if (userDto.getQualifications() != null) {
            user.getQualifications().clear();
            for (QualificationDto qualificationDto : userDto.getQualifications()) {
                Qualification qualification;
                if (qualificationDto.getId() != null) {
                    qualification = qualificationRepository.findById(qualificationDto.getId()).orElse(new Qualification());
                } else {
                    qualification = new Qualification();
                }
                qualification = qualificationMapper.mapToEntity(qualificationDto, qualification);
                qualification.setUser(user);  // Set the user reference
                user.getQualifications().add(qualification);
            }
        }

        if (userDto.getCv() != null) {
            user.setCv(attachmentMapper.mapToEntity(userDto.getCv(), new Attachment()));
        }

        if (userDto.getProfilePicture() != null) {
            user.setProfilePicture(attachmentMapper.mapToEntity(userDto.getProfilePicture(), new Attachment()));
        }

        user.setArchived(userDto.isArchived());

        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getBio() != null) {
            user.setBio(userDto.getBio());
        }
        if (userDto.getGithubUrl() != null) {
            user.setGithubUrl(userDto.getGithubUrl());
        }
        if (userDto.getLinkedinUrl() != null) {
            user.setLinkedinUrl(userDto.getLinkedinUrl());
        }
        if(userDto.getUserType() != null) {
            user.setUserType(userDto.getUserType());
        }
        return user;
    }
}
