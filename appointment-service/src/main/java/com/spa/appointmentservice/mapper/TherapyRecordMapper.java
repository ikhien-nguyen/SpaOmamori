package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.response.TherapyRecordResponse;
import com.spa.appointmentservice.entity.TherapyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TherapyRecordMapper {

    @Mapping(target = "appointmentId", source = "appointment.id")
    @Mapping(target = "therapyProfileId", source = "therapyProfile.id")
    TherapyRecordResponse toTherapyRecordResponse(TherapyRecord therapyRecord);
}
