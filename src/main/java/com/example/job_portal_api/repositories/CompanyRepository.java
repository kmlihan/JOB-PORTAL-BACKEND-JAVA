package com.example.job_portal_api.repositories;

import com.example.job_portal_api.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaSpecificationExecutor<Company>, JpaRepository<Company, UUID> {

    Optional<Company> findByEmail(String email);

}
