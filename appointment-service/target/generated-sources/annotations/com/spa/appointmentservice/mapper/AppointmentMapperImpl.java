package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.response.AppointmentResponse;
import com.spa.appointmentservice.entity.Appointment;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:05:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class AppointmentMapperImpl implements AppointmentMapper {

    @Override
    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        if ( appointment == null ) {
            return null;
        }

        AppointmentResponse.AppointmentResponseBuilder appointmentResponse = AppointmentResponse.builder();

        appointmentResponse.id( appointment.getId() );
        appointmentResponse.customerId( appointment.getCustomerId() );
        appointmentResponse.therapistId( appointment.getTherapistId() );
        appointmentResponse.serviceId( appointment.getServiceId() );
        appointmentResponse.serviceName( appointment.getServiceName() );
        appointmentResponse.servicePrice( appointment.getServicePrice() );
        appointmentResponse.roomId( appointment.getRoomId() );
        appointmentResponse.roomName( appointment.getRoomName() );
        appointmentResponse.roomPrice( appointment.getRoomPrice() );
        appointmentResponse.totalAmount( appointment.getTotalAmount() );
        appointmentResponse.appointmentTime( appointment.getAppointmentTime() );
        appointmentResponse.reason( appointment.getReason() );
        appointmentResponse.note( appointment.getNote() );
        appointmentResponse.status( appointment.getStatus() );
        appointmentResponse.createdAt( appointment.getCreatedAt() );

        return appointmentResponse.build();
    }
}
