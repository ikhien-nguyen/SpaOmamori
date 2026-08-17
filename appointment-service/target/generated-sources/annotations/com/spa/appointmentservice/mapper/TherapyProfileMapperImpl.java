package com.spa.appointmentservice.mapper;

import com.spa.appointmentservice.dto.request.CreateTherapyProfileRequest;
import com.spa.appointmentservice.dto.request.UpdateTherapyProfileRequest;
import com.spa.appointmentservice.dto.response.TherapyProfileResponse;
import com.spa.appointmentservice.entity.TherapyProfile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T20:05:35+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class TherapyProfileMapperImpl implements TherapyProfileMapper {

    @Override
    public TherapyProfile toTherapyProfile(CreateTherapyProfileRequest request) {
        if ( request == null ) {
            return null;
        }

        TherapyProfile.TherapyProfileBuilder therapyProfile = TherapyProfile.builder();

        therapyProfile.initialSkinCondition( request.getInitialSkinCondition() );
        therapyProfile.medicalHistory( request.getMedicalHistory() );

        return therapyProfile.build();
    }

    @Override
    public TherapyProfileResponse toTherapyProfileResponse(TherapyProfile profile) {
        if ( profile == null ) {
            return null;
        }

        TherapyProfileResponse.TherapyProfileResponseBuilder therapyProfileResponse = TherapyProfileResponse.builder();

        therapyProfileResponse.id( profile.getId() );
        therapyProfileResponse.customerId( profile.getCustomerId() );
        therapyProfileResponse.initialSkinCondition( profile.getInitialSkinCondition() );
        therapyProfileResponse.medicalHistory( profile.getMedicalHistory() );
        therapyProfileResponse.createdAt( profile.getCreatedAt() );
        therapyProfileResponse.updatedAt( profile.getUpdatedAt() );

        return therapyProfileResponse.build();
    }

    @Override
    public void updateTherapyProfile(TherapyProfile profile, UpdateTherapyProfileRequest request) {
        if ( request == null ) {
            return;
        }

        profile.setInitialSkinCondition( request.getInitialSkinCondition() );
        profile.setMedicalHistory( request.getMedicalHistory() );
    }
}
