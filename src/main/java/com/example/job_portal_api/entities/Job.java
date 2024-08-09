package com.example.job_portal_api.entities;

import com.example.job_portal_api.enums.ContractType;
import com.example.job_portal_api.enums.JobType;
import com.example.job_portal_api.enums.LevelOfEducation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "jobs")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Job {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String title;
    @Column(length = 10000)
    private String description;
    private double experience;
    private double salary;

    @ElementCollection
    @CollectionTable(name = "job_skills", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "skill")

    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "job_responsibilities", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "responsibility")

    private List<String> responsibilities = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "job_benefits", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "benefit")

    private List<String> benefits = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Enumerated(EnumType.STRING)
    private LevelOfEducation levelOfEducation;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> applicants = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @ElementCollection
    @CollectionTable(name = "job_languages", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "language")

    private List<String> languages = new ArrayList<>();

    @ColumnDefault("'false'")
    private boolean archived;

    @ColumnDefault("'false'")
    private boolean isFeatured;

    private String companyName;
    private String companyLogo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdDate = LocalDate.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate updatedDate = LocalDate.now();
}
