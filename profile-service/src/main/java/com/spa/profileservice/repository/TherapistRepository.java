package com.spa.profileservice.repository;

import com.spa.profileservice.entity.Therapist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TherapistRepository
        extends JpaRepository<Therapist, String> {

    Optional<Therapist> findByUserId(String userId);

    boolean existsByUserId(String userId);
}