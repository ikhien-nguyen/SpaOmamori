package com.spa.profileservice.service;

import com.spa.profileservice.dto.response.TherapistResponse;
import com.spa.profileservice.entity.Profile;
import com.spa.profileservice.entity.Therapist;
import com.spa.profileservice.repository.ProfileRepository;
import com.spa.profileservice.repository.TherapistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TherapistQueryService {

    TherapistRepository therapistRepository;
    ProfileRepository profileRepository;

    public List<TherapistResponse> getActiveTherapists() {

        // Profile và Therapist nằm cùng profile-service nhưng là 2 bảng khác nhau.
        // Ghép logic thông qua userId.
        Map<String, Profile> profilesByUserId =
                profileRepository.findAll()
                        .stream()
                        .collect(Collectors.toMap(
                                Profile::getUserId,
                                Function.identity(),
                                (first, second) -> first
                        ));

        return therapistRepository.findAll()
                .stream()
                .filter(Therapist::isActive)
                .map(therapist -> {

                    Profile profile =
                            profilesByUserId.get(therapist.getUserId());

                    if (profile == null) {
                        log.warn(
                                "Không tìm thấy Profile cho Therapist id={}, userId={}",
                                therapist.getId(),
                                therapist.getUserId()
                        );
                        return null;
                    }

                    return TherapistResponse.builder()
                            .id(therapist.getId())
                            .userId(therapist.getUserId())
                            .fullName(profile.getFullName())
                            .specialization(therapist.getSpecialization())
                            .certificate(therapist.getCertificate())
                            .experience(therapist.getExperience())
                            .avatarUrl(profile.getAvatarUrl())
                            .active(therapist.isActive())
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
