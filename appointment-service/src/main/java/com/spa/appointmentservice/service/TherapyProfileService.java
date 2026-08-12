package com.spa.appointmentservice.service;

import com.spa.appointmentservice.dto.request.CreateTherapyProfileRequest;
import com.spa.appointmentservice.dto.request.UpdateTherapyProfileRequest;
import com.spa.appointmentservice.dto.response.TherapyProfileResponse;
import com.spa.appointmentservice.entity.TherapyProfile;
import com.spa.appointmentservice.exception.AppException;
import com.spa.appointmentservice.exception.ErrorCode;
import com.spa.appointmentservice.mapper.TherapyProfileMapper;
import com.spa.appointmentservice.repository.TherapyProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TherapyProfileService {

    TherapyProfileRepository therapyProfileRepository;
    TherapyProfileMapper therapyProfileMapper;

    // Goi tu appointment-service khi khach hang lan dau tien tao ho so (hoac
    // tu dong tao rong khi lich hen dau tien hoan thanh, tuy nghiep vu ban chon).
    public TherapyProfileResponse createProfile(String customerId, CreateTherapyProfileRequest request) {
        if (therapyProfileRepository.existsByCustomerId(customerId)) {
            throw new AppException(ErrorCode.THERAPY_PROFILE_EXISTED);
        }

        TherapyProfile profile = therapyProfileMapper.toTherapyProfile(request);
        profile.setCustomerId(customerId);
        therapyProfileRepository.save(profile);
        return therapyProfileMapper.toTherapyProfileResponse(profile);
    }

    public TherapyProfileResponse getByCustomerId(String customerId) {
        TherapyProfile profile = therapyProfileRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.THERAPY_PROFILE_NOT_EXISTED));
        return therapyProfileMapper.toTherapyProfileResponse(profile);
    }

    public TherapyProfileResponse updateProfile(String customerId, UpdateTherapyProfileRequest request) {
        TherapyProfile profile = therapyProfileRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.THERAPY_PROFILE_NOT_EXISTED));

        therapyProfileMapper.updateTherapyProfile(profile, request);
        therapyProfileRepository.save(profile);
        return therapyProfileMapper.toTherapyProfileResponse(profile);
    }

    // Dung noi bo trong TherapyRecordService de lay/tao ho so neu chua co,
    // tranh KTV phai tao thu cong truoc khi ghi nhat ky lan dau.
    public TherapyProfile getOrCreateProfile(String customerId) {
        return therapyProfileRepository.findByCustomerId(customerId)
                .orElseGet(() -> therapyProfileRepository.save(
                        TherapyProfile.builder().customerId(customerId).build()));
    }
}
