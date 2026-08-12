package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.request.CreateTherapyProfileRequest;
import com.spa.appointmentservice.dto.response.TherapyProfileResponse;
import com.spa.appointmentservice.entity.TherapyProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.spa.appointmentservice.dto.request.UpdateTherapyProfileRequest;

@Mapper(componentModel = "spring")
public interface TherapyProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    TherapyProfile toTherapyProfile(CreateTherapyProfileRequest request);

    TherapyProfileResponse toTherapyProfileResponse(TherapyProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    void updateTherapyProfile(@MappingTarget TherapyProfile profile, UpdateTherapyProfileRequest request);
}
