package com.spa.treatmentservice.service;

import com.spa.treatmentservice.dto.request.TreatmentCreationRequest;
import com.spa.treatmentservice.dto.response.TreatmentResponse;
import com.spa.treatmentservice.entity.Treatment;
import com.spa.treatmentservice.exception.AppException;
import com.spa.treatmentservice.exception.ErrorCode;
import com.spa.treatmentservice.mapper.TreatmentMapper;
import com.spa.treatmentservice.repository.TreatmentRepository;
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
public class TreatmentService {

    TreatmentRepository treatmentRepository;
    TreatmentMapper treatmentMapper;

    public TreatmentResponse createTreatment(TreatmentCreationRequest request) {
        // UC_10 - dieu kien dac biet: "Ten dich vu/phong/my pham khong duoc
        // trung lap" -> check truoc khi luu, khong phan biet hoa/thuong.
        if (treatmentRepository.existsByNameIgnoreCase(request.getName())) {
            throw new AppException(ErrorCode.TREATMENT_NAME_EXISTED);
        }

        Treatment treatment = treatmentMapper.toTreatment(request);
        treatment.setIsActive(true);
        treatmentRepository.save(treatment);
        return treatmentMapper.toTreatmentResponse(treatment);
    }

    public List<TreatmentResponse> getAllActiveTreatments() {
        return treatmentRepository.findAll().stream()
                .filter(Treatment::getIsActive)
                .map(treatmentMapper::toTreatmentResponse)
                .toList();
    }

    public TreatmentResponse getTreatmentById(String id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_NOT_EXISTED));
        return treatmentMapper.toTreatmentResponse(treatment);
    }

    public void deactivateTreatment(String id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TREATMENT_NOT_EXISTED));
        treatment.setIsActive(false);
        treatmentRepository.save(treatment);
    }
}