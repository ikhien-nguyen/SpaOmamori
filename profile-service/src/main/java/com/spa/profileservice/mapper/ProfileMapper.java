package com.spa.profileservice.mapper;

import com.spa.profileservice.dto.request.CreateProfileRequest;
import com.spa.profileservice.dto.request.UpdateProfileRequest;
import com.spa.profileservice.dto.response.ProfileResponse;
import com.spa.profileservice.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mapping(target = "id", ignore = true)
    Profile toProfile(CreateProfileRequest request);

    ProfileResponse toProfileResponse(Profile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateProfile(@MappingTarget Profile profile, UpdateProfileRequest request);
}