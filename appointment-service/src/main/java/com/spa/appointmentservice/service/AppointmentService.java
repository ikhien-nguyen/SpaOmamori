package com.spa.appointmentservice.service;

import com.spa.appointmentservice.dto.request.AppointmentCreationRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentStatusRequest;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.entity.Appointment;
import com.spa.appointmentservice.entity.AppointmentStatus;
import com.spa.appointmentservice.entity.SpaService;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.mapper.AppointmentMapper;
import com.spa.appointmentservice.repository.AppointmentRepository;
import com.spa.appointmentservice.repository.ServiceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    ServiceRepository serviceRepository;
    AppointmentMapper appointmentMapper;

    public AppointmentResponse createAppointment(String customerId, AppointmentCreationRequest request) {
        SpaService service = serviceRepository.findById(request.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));

        if (request.getTherapistId() != null
                && appointmentRepository.existsByTherapistIdAndAppointmentTime(
                request.getTherapistId(), request.getAppointmentTime())) {
            throw new AppException(ErrorCode.APPOINTMENT_SLOT_TAKEN);
        }

        Appointment appointment = Appointment.builder()
                .customerId(customerId)
                .therapistId(request.getTherapistId())
                .service(service)
                .appointmentTime(request.getAppointmentTime())
                .reason(request.getReason())
                .note(request.getNote())
                .status(AppointmentStatus.PENDING)
                .build();

        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public List<AppointmentResponse> getAppointmentsByCustomer(String customerId) {
        return appointmentRepository.findByCustomerId(customerId).stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    public List<AppointmentResponse> getAppointmentsByTherapist(String therapistId) {
        return appointmentRepository.findByTherapistId(therapistId).stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public AppointmentResponse updateStatus(String id, UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));

        validateStatusTransition(appointment.getStatus(), request.getStatus());

        appointment.setStatus(request.getStatus());
        if (request.getTherapistId() != null) {
            appointment.setTherapistId(request.getTherapistId());
        }

        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // Không cho nhảy trạng thái tùy ý (VD: từ CANCELLED quay lại PENDING)
    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        boolean invalid = switch (current) {
            case PENDING -> next == AppointmentStatus.PENDING;
            case IN_PROGRESS -> next == AppointmentStatus.PENDING || next == AppointmentStatus.IN_PROGRESS;
            case COMPLETED, CANCELLED -> true; // trạng thái cuối, không đổi được nữa
        };

        if (invalid) {
            throw new AppException(ErrorCode.INVALID_APPOINTMENT_STATUS_TRANSITION);
        }
    }
}