package com.spa.treatmentservice.mapper;

import com.spa.treatmentservice.dto.request.TreatmentCreationRequest;
import com.spa.treatmentservice.dto.response.TreatmentResponse;
import com.spa.treatmentservice.entity.Treatment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    Treatment toTreatment(TreatmentCreationRequest request);

    TreatmentResponse toTreatmentResponse(Treatment treatment);
}