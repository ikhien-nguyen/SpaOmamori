package com.spa.appointmentservice.repository;

import com.spa.appointmentservice.entity.Appointment;
import com.spa.appointmentservice.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    List<Appointment> findByCustomerId(String customerId);

    List<Appointment> findByTherapistId(String therapistId);

    // Lay cac lich hen (chua huy) cua KTV trong 1 khung ngay de Service tinh
    // chong lan khung gio o tang Java — khong dung existsBy...AppointmentTime
    // (so trung tuyet doi) vi khong the phat hien 2 lich hen khac gio nhung
    // van de len nhau ve thoi luong dich vu.
    List<Appointment> findByTherapistIdAndStatusNotAndAppointmentTimeBetween(
            String therapistId, AppointmentStatus excludedStatus, LocalDateTime from, LocalDateTime to);

    // Tuong tu cho Phong: chong double-booking phong trong cung khung ngay.
    List<Appointment> findByRoomIdAndStatusNotAndAppointmentTimeBetween(
            String roomId, AppointmentStatus excludedStatus, LocalDateTime from, LocalDateTime to);
}