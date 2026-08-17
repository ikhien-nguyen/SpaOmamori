package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.response.TherapyRecordResponse;
import com.spa.appointmentservice.entity.Appointment;
import com.spa.appointmentservice.entity.TherapyProfile;
import com.spa.appointmentservice.entity.TherapyRecord;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:05:35+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class TherapyRecordMapperImpl implements TherapyRecordMapper {

    @Override
    public TherapyRecordResponse toTherapyRecordResponse(TherapyRecord therapyRecord) {
        if ( therapyRecord == null ) {
            return null;
        }

        TherapyRecordResponse.TherapyRecordResponseBuilder therapyRecordResponse = TherapyRecordResponse.builder();

        therapyRecordResponse.appointmentId( therapyRecordAppointmentId( therapyRecord ) );
        therapyRecordResponse.therapyProfileId( therapyRecordTherapyProfileId( therapyRecord ) );
        therapyRecordResponse.id( therapyRecord.getId() );
        therapyRecordResponse.conditionNotes( therapyRecord.getConditionNotes() );
        therapyRecordResponse.improvementNotes( therapyRecord.getImprovementNotes() );
        therapyRecordResponse.remainingSessions( therapyRecord.getRemainingSessions() );
        therapyRecordResponse.recordedAt( therapyRecord.getRecordedAt() );

        return therapyRecordResponse.build();
    }

    private String therapyRecordAppointmentId(TherapyRecord therapyRecord) {
        if ( therapyRecord == null ) {
            return null;
        }
        Appointment appointment = therapyRecord.getAppointment();
        if ( appointment == null ) {
            return null;
        }
        String id = appointment.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String therapyRecordTherapyProfileId(TherapyRecord therapyRecord) {
        if ( therapyRecord == null ) {
            return null;
        }
        TherapyProfile therapyProfile = therapyRecord.getTherapyProfile();
        if ( therapyProfile == null ) {
            return null;
        }
        String id = therapyProfile.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
