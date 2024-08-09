package com.example.job_portal_api.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "countries")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Country {
    @Id
    private long id;
    private String iso;

    private String name;

    private String niceName;

    private String iso3;

    private String numcode;

    private String phonecode;

    private String nisCode;
}
