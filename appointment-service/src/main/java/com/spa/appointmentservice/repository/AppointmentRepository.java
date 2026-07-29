package com.spa.appointmentservice.repository;

import com.spa.appointmentservice.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByCustomerId(String customerId);

    List<Appointment> findByTherapistId(String therapistId);

    // Chống double-booking: kiểm tra therapist đã có lịch hẹn khác trong khoảng thời gian này chưa
    boolean existsByTherapistIdAndAppointmentTime(String therapistId, LocalDateTime appointmentTime);
}