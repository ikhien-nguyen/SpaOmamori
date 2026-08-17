package com.spa.appointmentservice.service;

import com.spa.appointmentservice.dto.request.CreateTherapyRecordRequest;
import com.spa.appointmentservice.dto.response.TherapyRecordResponse;
import com.spa.appointmentservice.entity.Appointment;
import com.spa.appointmentservice.entity.TherapyProfile;
import com.spa.appointmentservice.entity.TherapyRecord;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.mapper.TherapyRecordMapper;
import com.spa.appointmentservice.repository.AppointmentRepository;
import com.spa.appointmentservice.repository.TherapyRecordRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TherapyRecordService {

    TherapyRecordRepository therapyRecordRepository;
    AppointmentRepository appointmentRepository;
    TherapyRecordMapper therapyRecordMapper;
    TherapyProfileService therapyProfileService;
    AppointmentService appointmentService; // de dung chung 1 luat chuyen trang thai + tra phong

    public TherapyRecordResponse createRecord(String appointmentId, CreateTherapyRecordRequest request) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));

        if (therapyRecordRepository.existsByAppointmentId(appointmentId)) {
            throw new AppException(ErrorCode.THERAPY_RECORD_EXISTED);
        }

        // Lay ho so goc cua khach hang, tu dong tao rong neu day la lan dau
        // (KTV khong phai thao tac tao ho so thu cong truoc)
        TherapyProfile profile = therapyProfileService.getOrCreateProfile(appointment.getCustomerId());

        TherapyRecord record = TherapyRecord.builder()
                .appointment(appointment)
                .therapyProfile(profile)
                // Chốt technicianId từ chính lịch hẹn tại thời điểm ghi nhật ký (xem
                // giải thích ở entity vì sao không để MapStruct/JPA tự suy ra lại sau này).
                .technicianId(appointment.getTherapistId())
                .conditionNotes(request.getConditionNotes())
                .improvementNotes(request.getImprovementNotes())
                .remainingSessions(request.getRemainingSessions())
                .build();

        therapyRecordRepository.save(record);

        // Chi cho lap nhat ky khi lich hen dang IN_PROGRESS (dung UC_08 -
        // "sau ca lam viec"), va di qua AppointmentService.completeAppointment
        // de dam bao khong bo qua validateStatusTransition + tra phong lai.
        appointmentService.completeAppointment(appointment);

        return therapyRecordMapper.toTherapyRecordResponse(record);
    }

    public TherapyRecordResponse getRecordByAppointmentId(String appointmentId) {
        TherapyRecord record = therapyRecordRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_EXISTED));
        return therapyRecordMapper.toTherapyRecordResponse(record);
    }
}