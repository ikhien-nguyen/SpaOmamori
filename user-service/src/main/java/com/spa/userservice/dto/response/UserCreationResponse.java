package com.spa.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreationResponse {
    private String fullName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String gender;
    private String phone;
    private String address;
}
