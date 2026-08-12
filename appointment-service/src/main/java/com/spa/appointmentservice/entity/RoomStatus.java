package com.spa.appointmentservice.entity;

/**
 * Mirror của RoomStatus bên room-service — dùng khi appointment-service gọi
 * sang RoomClient để đồng bộ trạng thái trống/bận của phòng
 */
public enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
}