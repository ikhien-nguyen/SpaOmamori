package com.spa.appointmentservice.client;

import com.spa.appointmentservice.dto.response.ApiResponse;
import com.spa.appointmentservice.dto.response.RoomResponse;
import com.spa.appointmentservice.entity.RoomStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "room-service", url = "${room-service.url}")
public interface RoomClient {

    @GetMapping("/rooms/internal/{id}")
    ApiResponse<RoomResponse> getRoom(@PathVariable("id") String id);

    // Dong bo trang/ban cua phong theo trang thai lich hen (UC_07 - hau dieu
    // kien). Loi goi sang day khong duoc lam fail ca luong doi trang thai
    // lich hen - AppointmentService se tu bat va chi log warning.
    @PatchMapping("/rooms/internal/{id}/status")
    ApiResponse<RoomResponse> updateRoomStatus(@PathVariable("id") String id, @RequestParam("status") RoomStatus status);
}