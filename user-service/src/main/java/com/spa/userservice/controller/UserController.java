package com.spa.userservice.controller;

import com.spa.userservice.dto.request.UserCreationRequest;
import com.spa.userservice.dto.request.LoginRequest;
import com.spa.userservice.dto.response.ApiResponse;
import com.spa.userservice.dto.response.LoginResponse;
import com.spa.userservice.dto.response.UserCreationResponse;
import com.spa.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/create")
    public ApiResponse<UserCreationResponse> createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return ApiResponse.<UserCreationResponse>builder()
                .result(userService.createUser(userCreationRequest))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ApiResponse.<LoginResponse>builder()
                .result(new LoginResponse(token))
                .build();
    }

}
