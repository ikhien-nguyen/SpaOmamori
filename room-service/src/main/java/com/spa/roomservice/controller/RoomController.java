package com.spa.roomservice.controller;

import com.spa.roomservice.dto.request.RoomCreationRequest;
import com.spa.roomservice.dto.response.ApiResponse;
import com.spa.roomservice.dto.response.RoomResponse;
import com.spa.roomservice.entity.RoomStatus;
import com.spa.roomservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoomController {

    RoomService roomService;

    @PostMapping
    public ApiResponse<RoomResponse> createRoom(@Valid @RequestBody RoomCreationRequest request) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.createRoom(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<RoomResponse>> getAllRooms() {
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getAllActiveRooms())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomResponse> getRoom(@PathVariable String id) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.getRoomById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deactivateRoom(@PathVariable String id) {
        roomService.deactivateRoom(id);
        return ApiResponse.<Void>builder().message("Đã ngừng sử dụng phòng").build();
    }

    // Endpoint nội bộ cho appointment-service gọi sang để validate + lấy giá khi đặt lịch
    @GetMapping("/internal/{id}")
    public ApiResponse<RoomResponse> getRoomInternal(@PathVariable String id) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.getRoomById(id))
                .build();
    }

    // Endpoint nội bộ: appointment-service gọi sang để đồng bộ trạng thái
    // trống/bận của phòng theo trạng thái lịch hẹn (UC_07 - hậu điều kiện).
    @PatchMapping("/internal/{id}/status")
    public ApiResponse<RoomResponse> updateRoomStatusInternal(
            @PathVariable String id, @RequestParam RoomStatus status) {
        return ApiResponse.<RoomResponse>builder()
                .result(roomService.updateRoomStatus(id, status))
                .build();
    }
}