package com.spa.roomservice.entity;

/**
 * Trạng thái sử dụng phòng tại thời điểm hiện tại — khác với isActive (phòng
 * có còn nằm trong danh mục kinh doanh hay không). Được appointment-service
 * gọi sang cập nhật khi lịch hẹn chuyển IN_PROGRESS (chiếm phòng) hoặc
 * COMPLETED/CANCELLED (trả phòng), theo đúng hậu điều kiện UC_07.
 */
public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
}