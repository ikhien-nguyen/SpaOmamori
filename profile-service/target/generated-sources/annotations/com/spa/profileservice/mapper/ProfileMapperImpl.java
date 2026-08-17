package com.spa.profileservice.mapper;

import com.spa.profileservice.dto.request.CreateProfileRequest;
import com.spa.profileservice.dto.request.UpdateProfileRequest;
import com.spa.profileservice.dto.response.ProfileResponse;
import com.spa.profileservice.entity.Gender;
import com.spa.profileservice.entity.Profile;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-17T19:59:56+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23-valhalla (Oracle Corporation)"
)
@Component
public class ProfileMapperImpl implements ProfileMapper {

    @Override
    public Profile toProfile(CreateProfileRequest request) {
        if ( request == null ) {
            return null;
        }

        Profile.ProfileBuilder profile = Profile.builder();

        profile.userId( request.getUserId() );
        profile.dateOfBirth( request.getDateOfBirth() );
        if ( request.getGender() != null ) {
            profile.gender( Enum.valueOf( Gender.class, request.getGender() ) );
        }
        profile.phone( request.getPhone() );
        profile.address( request.getAddress() );

        return profile.build();
    }

    @Override
    public ProfileResponse toProfileResponse(Profile profile) {
        if ( profile == null ) {
            return null;
        }

        ProfileResponse.ProfileResponseBuilder profileResponse = ProfileResponse.builder();

        profileResponse.id( profile.getId() );
        profileResponse.userId( profile.getUserId() );
        profileResponse.dateOfBirth( profile.getDateOfBirth() );
        if ( profile.getGender() != null ) {
            profileResponse.gender( profile.getGender().name() );
        }
        profileResponse.phone( profile.getPhone() );
        profileResponse.address( profile.getAddress() );
        profileResponse.avatarUrl( profile.getAvatarUrl() );

        return profileResponse.build();
    }

    @Override
    public void updateProfile(Profile profile, UpdateProfileRequest request) {
        if ( request == null ) {
            return;
        }

        profile.setDateOfBirth( request.getDateOfBirth() );
        if ( request.getGender() != null ) {
            profile.setGender( Enum.valueOf( Gender.class, request.getGender() ) );
        }
        else {
            profile.setGender( null );
        }
        profile.setPhone( request.getPhone() );
        profile.setAddress( request.getAddress() );
        profile.setAvatarUrl( request.getAvatarUrl() );
    }
}
