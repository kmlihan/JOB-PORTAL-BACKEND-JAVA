package com.example.job_portal_api.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "addresses")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Address {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
    @Column
    private String city;
    @Column
    private String postalCode;
    @Column
    private String street;
    @Column
    private String houseNumber;
    @Column
    private String box;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
    @LastModifiedDate
    private LocalDateTime lastModifiedDate = LocalDateTime.now();
    @ManyToOne
    @JoinColumn(name = "country", referencedColumnName = "iso")
    private Country country;

    @Override
    public String toString() {
        return street  + " " + houseNumber  + "\n" +
                postalCode  + " " + city  + "\n" +
                (country != null ? country.getName() : "null");
    }


}
