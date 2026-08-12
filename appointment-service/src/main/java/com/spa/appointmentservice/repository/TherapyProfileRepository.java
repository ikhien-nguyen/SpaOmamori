package com.spa.appointmentservice.repository;

import com.spa.appointmentservice.entity.TherapyProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TherapyProfileRepository extends JpaRepository<TherapyProfile, String> {
    boolean existsByCustomerId(String customerId);

    Optional<TherapyProfile> findByCustomerId(String customerId);
}
