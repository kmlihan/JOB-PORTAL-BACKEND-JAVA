package com.example.job_portal_api.repositories;

import com.example.job_portal_api.entities.Qualification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QualificationRepository extends JpaRepository<Qualification, UUID> {
}
