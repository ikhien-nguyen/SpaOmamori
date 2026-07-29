package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.request.ServiceCreationRequest;
import com.spa.appointmentservice.dto.response.ServiceResponse;
import com.spa.appointmentservice.entity.SpaService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true) // mặc định true, set ở Service khi tạo mới
    SpaService toSpaService(ServiceCreationRequest request);

    ServiceResponse toServiceResponse(SpaService service);
}