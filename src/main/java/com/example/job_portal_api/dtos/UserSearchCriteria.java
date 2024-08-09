package com.example.job_portal_api.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchCriteria {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String nationality;
    private String profileTitle;
    private List<String> skills;
    private List<String> languages;
    private Boolean archived;
    private String city;
    private String country;
}
