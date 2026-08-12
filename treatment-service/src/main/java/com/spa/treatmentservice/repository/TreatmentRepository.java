package com.spa.treatmentservice.repository;

import com.spa.treatmentservice.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, String> {
    boolean existsByNameIgnoreCase(String name);
}