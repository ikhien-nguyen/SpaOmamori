package com.spa.profileservice.service;

import com.spa.profileservice.dto.request.CreateProfileRequest;
import com.spa.profileservice.dto.request.UpdateProfileRequest;
import com.spa.profileservice.dto.response.ProfileResponse;
import com.spa.profileservice.entity.Profile;
import com.spa.profileservice.exception.AppException;
import com.spa.profileservice.exception.ErrorCode;
import com.spa.profileservice.mapper.ProfileMapper;
import com.spa.profileservice.repository.ProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ProfileService {

    ProfileRepository profileRepository;
    ProfileMapper profileMapper;

    public ProfileResponse createProfile(CreateProfileRequest request) {
        if (profileRepository.existsByUserId(request.getUserId())) {
            throw new AppException(ErrorCode.PROFILE_EXISTED);
        }

        Profile profile = profileMapper.toProfile(request);
        profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse getProfileByUserId(String userId) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));

        return profileMapper.toProfileResponse(profile);
    }

    public ProfileResponse updateProfile(String userId, UpdateProfileRequest request) {
        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.PROFILE_NOT_EXISTED));

        profileMapper.updateProfile(profile, request);
        profileRepository.save(profile);

        return profileMapper.toProfileResponse(profile);
    }
}