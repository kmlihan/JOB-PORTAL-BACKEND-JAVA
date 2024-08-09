package com.example.job_portal_api.repositories;

import com.example.job_portal_api.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country,Long> {
    Country findCountryByIso(String iso);
}
