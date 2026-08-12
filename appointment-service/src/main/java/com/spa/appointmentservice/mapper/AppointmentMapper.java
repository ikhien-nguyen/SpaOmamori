package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.entity.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {
    AppointmentResponse toAppointmentResponse(Appointment appointment);
}
