package com.spa.appointmentservice.repository;

import com.spa.appointmentservice.entity.TherapyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TherapyRecordRepository extends JpaRepository<TherapyRecord, String> {
    boolean existsByAppointmentId(String appointmentId);

    Optional<TherapyRecord> findByAppointmentId(String appointmentId);
}
