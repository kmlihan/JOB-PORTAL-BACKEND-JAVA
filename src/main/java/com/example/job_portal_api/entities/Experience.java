package com.example.job_portal_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "experiences")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Experience {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    private String jobTitle;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companyName;
    private String roleDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
