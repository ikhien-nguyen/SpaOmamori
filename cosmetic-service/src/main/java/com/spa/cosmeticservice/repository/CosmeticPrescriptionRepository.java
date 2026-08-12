package com.spa.cosmeticservice.repository;

import com.spa.cosmeticservice.entity.CosmeticPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CosmeticPrescriptionRepository extends JpaRepository<CosmeticPrescription, String> {
    List<CosmeticPrescription> findByAppointmentId(String appointmentId);
}
