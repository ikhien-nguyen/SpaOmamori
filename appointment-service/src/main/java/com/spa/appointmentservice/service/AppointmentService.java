package com.spa.appointmentservice.service;

import com.spa.appointmentservice.client.RoomClient;
import com.spa.appointmentservice.client.TreatmentClient;
import com.spa.appointmentservice.dto.request.AppointmentCreationRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentRequest;
import com.spa.appointmentservice.dto.request.UpdateAppointmentStatusRequest;
import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.dto.response.RoomResponse;
import com.spa.appointmentservice.dto.response.TreatmentResponse;
import com.spa.appointmentservice.entity.Appointment;
import com.spa.appointmentservice.entity.AppointmentStatus;
import com.spa.appointmentservice.entity.RoomStatus;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.mapper.AppointmentMapper;
import com.spa.appointmentservice.repository.AppointmentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppointmentService {

    AppointmentRepository appointmentRepository;
    AppointmentMapper appointmentMapper;
    TreatmentClient treatmentClient;
    RoomClient roomClient;

    public AppointmentResponse createAppointment(String customerId, AppointmentCreationRequest request) {
        TreatmentResponse treatment = fetchTreatment(request.getServiceId());
        RoomResponse room = fetchRoom(request.getRoomId());

        checkNoConflict(
                request.getTherapistId(),
                request.getRoomId(),
                request.getAppointmentTime(),
                treatment.getDurationMinutes(),
                null);

        var totalAmount = treatment.getPrice().add(room.getPrice());

        Appointment appointment = Appointment.builder()
                .customerId(customerId)
                .therapistId(request.getTherapistId())
                .serviceId(treatment.getId())
                .serviceName(treatment.getName())
                .servicePrice(treatment.getPrice())
                .roomId(room.getId())
                .roomName(room.getName())
                .roomPrice(room.getPrice())
                .totalAmount(totalAmount)
                .appointmentTime(request.getAppointmentTime())
                .durationMinutes(treatment.getDurationMinutes())
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

    /**
     * Self-service: customer fetches their own appointment by id. Throws
     * APPOINTMENT_ACCESS_DENIED if the appointment does not belong to the
     * caller's customerId (derived from JWT subject upstream).
     */
    public AppointmentResponse getMyAppointmentById(String customerId, String id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));
        requireOwnership(appointment, customerId);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // UC_05 - luong thay the 3a-3d: khach hang sua lich hen cua chinh minh,
    // chi duoc phep khi con o trang thai PENDING. Phai kiem tra lai trung
    // lich (tru chinh no) vi gio/phong/KTV co the da doi.
    public AppointmentResponse updateAppointment(String id, String customerId, UpdateAppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));

        requireOwnership(appointment, customerId);

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_PENDING);
        }

        TreatmentResponse treatment = fetchTreatment(request.getServiceId());
        RoomResponse room = fetchRoom(request.getRoomId());

        checkNoConflict(
                request.getTherapistId(),
                request.getRoomId(),
                request.getAppointmentTime(),
                treatment.getDurationMinutes(),
                appointment.getId());

        appointment.setTherapistId(request.getTherapistId());
        appointment.setServiceId(treatment.getId());
        appointment.setServiceName(treatment.getName());
        appointment.setServicePrice(treatment.getPrice());
        appointment.setRoomId(room.getId());
        appointment.setRoomName(room.getName());
        appointment.setRoomPrice(room.getPrice());
        appointment.setTotalAmount(treatment.getPrice().add(room.getPrice()));
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDurationMinutes(treatment.getDurationMinutes());
        appointment.setReason(request.getReason());
        appointment.setNote(request.getNote());

        appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // UC_05 - luong thay the 3a1-3a4: khach hang huy lich hen cua chinh minh,
    // chi voi lich "chua dien ra" (con PENDING/CONFIRMED).
    public AppointmentResponse cancelAppointment(String id, String customerId) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));

        requireOwnership(appointment, customerId);
        validateStatusTransition(appointment.getStatus(), AppointmentStatus.CANCELLED);

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
        releaseRoomIfOccupied(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // Dung cho Admin/KTV cap nhat trang thai (UC_07): PENDING -> CONFIRMED ->
    // IN_PROGRESS -> COMPLETED, hoac CANCELLED tu PENDING/CONFIRMED.
    public AppointmentResponse updateStatus(String id, UpdateAppointmentStatusRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));

        validateStatusTransition(appointment.getStatus(), request.getStatus());

        appointment.setStatus(request.getStatus());
        if (request.getTherapistId() != null) {
            appointment.setTherapistId(request.getTherapistId());
        }

        appointmentRepository.save(appointment);

        // UC_07 - hau dieu kien: dong bo trang/ban cua phong theo trang thai
        // lich hen. IN_PROGRESS -> chiem phong; roi khoi IN_PROGRESS -> tra
        // phong lai (COMPLETED hoac CANCELLED).
        if (request.getStatus() == AppointmentStatus.IN_PROGRESS) {
            syncRoomStatus(appointment.getRoomId(), RoomStatus.OCCUPIED);
        } else if (request.getStatus() == AppointmentStatus.COMPLETED
                || request.getStatus() == AppointmentStatus.CANCELLED) {
            syncRoomStatus(appointment.getRoomId(), RoomStatus.AVAILABLE);
        }

        return appointmentMapper.toAppointmentResponse(appointment);
    }

    // Dung boi TherapyRecordService khi KTV luu nhat ky + ket thuc ca tri
    // lieu - gom tat ca luat (validate transition + tra phong) vao 1 cho duy
    // nhat, tranh TherapyRecordService set thang status bo qua kiem tra.
    public void completeAppointment(Appointment appointment) {
        if (appointment.getStatus() != AppointmentStatus.IN_PROGRESS) {
            throw new AppException(ErrorCode.APPOINTMENT_NOT_IN_PROGRESS);
        }
        validateStatusTransition(appointment.getStatus(), AppointmentStatus.COMPLETED);

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        releaseRoomIfOccupied(appointment);
    }

    private void requireOwnership(Appointment appointment, String customerId) {
        if (!appointment.getCustomerId().equals(customerId)) {
            throw new AppException(ErrorCode.APPOINTMENT_ACCESS_DENIED);
        }
    }

    private void releaseRoomIfOccupied(Appointment appointment) {
        syncRoomStatus(appointment.getRoomId(), RoomStatus.AVAILABLE);
    }

    private void syncRoomStatus(String roomId, RoomStatus status) {
        try {
            roomClient.updateRoomStatus(roomId, status);
        } catch (Exception e) {
            // Khong de loi goi sang room-service lam fail ca viec doi trang
            // thai lich hen - chi log canh bao, phong se lech trang thai tam
            // thoi va can doi soat thu cong / retry sau.
            log.warn("Dong bo trang thai phong {} that bai: {}", roomId, e.getMessage());
        }
    }

    // Kiem tra trung lich cho ca KTV lan Phong: 2 lich hen chong lan nhau ve
    // khung gio [appointmentTime, appointmentTime + durationMinutes), khong
    // tinh cac lich da CANCELLED. Chi lay du lieu trong pham vi 1 ngay cua
    // appointmentTime moi (du cho gio hoat dong cua spa) de tranh quet toan
    // bo lich su.
    private void checkNoConflict(
            String therapistId, String roomId, LocalDateTime start, Integer durationMinutes, String excludeAppointmentId) {
        LocalDateTime end = start.plusMinutes(durationMinutes);
        LocalDate day = start.toLocalDate();
        LocalDateTime dayStart = day.atStartOfDay();
        LocalDateTime dayEnd = dayStart.plusDays(1);

        if (therapistId != null) {
            var therapistAppointments = appointmentRepository.findByTherapistIdAndStatusNotAndAppointmentTimeBetween(
                    therapistId, AppointmentStatus.CANCELLED, dayStart, dayEnd);
            if (hasOverlap(therapistAppointments, start, end, excludeAppointmentId)) {
                throw new AppException(ErrorCode.THERAPIST_SLOT_TAKEN);
            }
        }

        var roomAppointments = appointmentRepository.findByRoomIdAndStatusNotAndAppointmentTimeBetween(
                roomId, AppointmentStatus.CANCELLED, dayStart, dayEnd);
        if (hasOverlap(roomAppointments, start, end, excludeAppointmentId)) {
            throw new AppException(ErrorCode.ROOM_SLOT_TAKEN);
        }
    }

    private boolean hasOverlap(
            List<Appointment> existingAppointments, LocalDateTime newStart, LocalDateTime newEnd, String excludeAppointmentId) {
        for (Appointment existing : existingAppointments) {
            if (excludeAppointmentId != null && excludeAppointmentId.equals(existing.getId())) {
                continue;
            }
            LocalDateTime existingStart = existing.getAppointmentTime();
            LocalDateTime existingEnd = existing.getEndTime();
            // 2 khung gio chong nhau khi: start moi < end cu VA start cu < end moi
            if (newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd)) {
                return true;
            }
        }
        return false;
    }

    private TreatmentResponse fetchTreatment(String serviceId) {
        try {
            var response = treatmentClient.getTreatment(serviceId);
            if (response.getResult() == null || !Boolean.TRUE.equals(response.getResult().getIsActive())) {
                throw new AppException(ErrorCode.SERVICE_NOT_EXISTED);
            }
            return response.getResult();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gọi treatment-service thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.SERVICE_NOT_EXISTED);
        }
    }

    private RoomResponse fetchRoom(String roomId) {
        try {
            var response = roomClient.getRoom(roomId);
            if (response.getResult() == null || !Boolean.TRUE.equals(response.getResult().getIsActive())) {
                throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
            }
            return response.getResult();
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            log.error("Gọi room-service thất bại: {}", e.getMessage());
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }
    }

    // Luong: PENDING -> CONFIRMED -> IN_PROGRESS -> COMPLETED
    //                 \-> CANCELLED (chi huy duoc tu PENDING/CONFIRMED,
    //                     KHONG huy duoc khi da IN_PROGRESS - dung theo UC_05:
    //                     "huy lich hen chua dien ra")
    // Whitelist tung buoc hop le - KHONG cho nhay coc (VD: PENDING thang len
    // COMPLETED) va KHONG cho lui/dung yen.
    private void validateStatusTransition(AppointmentStatus current, AppointmentStatus next) {
        boolean valid = switch (current) {
            case PENDING -> next == AppointmentStatus.CONFIRMED || next == AppointmentStatus.CANCELLED;
            case CONFIRMED -> next == AppointmentStatus.IN_PROGRESS || next == AppointmentStatus.CANCELLED;
            case IN_PROGRESS -> next == AppointmentStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false; // trang thai cuoi, khong doi duoc nua
        };

        if (!valid) {
            throw new AppException(ErrorCode.INVALID_APPOINTMENT_STATUS_TRANSITION);
        }
    }
}