package com.spa.userservice.controller;

import com.spa.userservice.dto.request.TherapistCreationRequest;
import com.spa.userservice.dto.response.ApiResponse;
import com.spa.userservice.dto.response.UserCreationResponse;
import com.spa.userservice.dto.response.UserResponse;
import com.spa.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UC_09 - Quản lý người dùng & nhân sự. Toàn bộ endpoint chỉ dành cho Admin.
 * Request path đầy đủ (qua Gateway): /api/omamori/users/**
 * (context-path /users của service này + root mapping "" ở đây).
 */
@RestController
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasAuthority('ADMIN')")
public class UserManagementController {

    UserService userService;

    // Admin tạo tài khoản Kỹ thuật viên .
    @PostMapping("/therapists ")
    public ApiResponse<UserCreationResponse> createStaff(@Valid @RequestBody TherapistCreationRequest request) {
        return ApiResponse.<UserCreationResponse>builder()
                .result(userService.createStaffUser(request))
                .build();
    }

    // Admin xem danh sách toàn bộ người dùng (Customer, Staff, Admin).
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    // Vô hiệu hóa tài khoản — user bị khóa sẽ không login được (xem UserService#login()).
    @PatchMapping("/{id}/lock")
    public ApiResponse<UserResponse> lockUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .message("Đã vô hiệu hóa tài khoản")
                .result(userService.setUserActive(id, false))
                .build();
    }

    @PatchMapping("/{id}/unlock")
    public ApiResponse<UserResponse> unlockUser(@PathVariable String id) {
        return ApiResponse.<UserResponse>builder()
                .message("Đã kích hoạt lại tài khoản")
                .result(userService.setUserActive(id, true))
                .build();
    }
}
