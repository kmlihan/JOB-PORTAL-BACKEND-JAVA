package com.example.job_portal_api.repositories;

import com.example.job_portal_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaSpecificationExecutor<User>, JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String token);
}
