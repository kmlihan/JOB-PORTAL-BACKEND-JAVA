package com.example.job_portal_api.entities;

import com.example.job_portal_api.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@Table(name = "companies")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Company {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String name;
    @Column(length = 10000)
    private String description;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    private String websiteUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "logo_id")
    private Attachment logo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs;
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.COMPANY;
}
