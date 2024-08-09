package com.example.job_portal_api.repositories;

import com.example.job_portal_api.entities.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaSpecificationExecutor<Job>, JpaRepository<Job, UUID> {
}
