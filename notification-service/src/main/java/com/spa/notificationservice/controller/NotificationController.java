package com.spa.notificationservice.controller;

import com.spa.notificationservice.dto.request.CreateNotificationRequest;
import com.spa.notificationservice.dto.response.ApiResponse;
import com.spa.notificationservice.dto.response.NotificationResponse;
import com.spa.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationController {

    NotificationService notificationService;

    @PostMapping
    public ApiResponse<NotificationResponse> create(@Valid @RequestBody CreateNotificationRequest request) {
        return ApiResponse.<NotificationResponse>builder()
                .message("Tạo thông báo thành công")
                .result(notificationService.create(request))
                .build();
    }

    // TAM THOI: nhan userId qua path vi CHUA co filter JWT o Gateway de gan
    // X-User-Id. Khi nao lam xac thuc that, doi lai thanh @RequestHeader.
    @GetMapping("/user/{userId}")
    public ApiResponse<List<NotificationResponse>> getByUser(@PathVariable String userId) {
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getMyNotifications(userId))
                .build();
    }

    @GetMapping("/user/{userId}/unread-count")
    public ApiResponse<Long> unreadCount(@PathVariable String userId) {
        return ApiResponse.<Long>builder()
                .result(notificationService.countUnread(userId))
                .build();
    }

    @PatchMapping("/{id}/read")
    public ApiResponse<NotificationResponse> markAsRead(
            @PathVariable String id, @RequestParam String userId) {
        return ApiResponse.<NotificationResponse>builder()
                .message("Đã đánh dấu đã đọc")
                .result(notificationService.markAsRead(id, userId))
                .build();
    }
}