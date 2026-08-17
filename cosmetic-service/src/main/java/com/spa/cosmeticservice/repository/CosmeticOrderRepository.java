package com.spa.cosmeticservice.repository;

import com.spa.cosmeticservice.entity.CosmeticOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CosmeticOrderRepository extends JpaRepository<CosmeticOrder, String> {
    // unique trên appointment_id -> tối đa 1 đơn kê / lịch hẹn, dùng Optional thay vì List.
    Optional<CosmeticOrder> findByAppointmentId(String appointmentId);
}
