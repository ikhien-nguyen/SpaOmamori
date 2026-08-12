package com.spa.appointmentservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    SERVICE_NOT_EXISTED(1020, "Không tìm thấy dịch vụ", HttpStatus.NOT_FOUND),
    ROOM_NOT_EXISTED(1025, "Không tìm thấy phòng", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_EXISTED(1021, "Không tìm thấy lịch hẹn", HttpStatus.NOT_FOUND),
    THERAPIST_SLOT_TAKEN(1022, "Kỹ thuật viên đã có lịch hẹn khác vào thời gian này", HttpStatus.BAD_REQUEST),
    ROOM_SLOT_TAKEN(1026, "Phòng đã được đặt vào thời gian này", HttpStatus.BAD_REQUEST),
    THERAPY_RECORD_EXISTED(1023, "Lịch hẹn này đã có nhật ký trị liệu", HttpStatus.BAD_REQUEST),
    INVALID_APPOINTMENT_STATUS_TRANSITION(1024, "Không thể chuyển sang trạng thái này", HttpStatus.BAD_REQUEST),
    THERAPY_PROFILE_EXISTED(1027, "Khách hàng này đã có hồ sơ trị liệu", HttpStatus.BAD_REQUEST),
    THERAPY_PROFILE_NOT_EXISTED(1028, "Không tìm thấy hồ sơ trị liệu", HttpStatus.NOT_FOUND),
    APPOINTMENT_ACCESS_DENIED(1029, "Bạn không có quyền thao tác trên lịch hẹn này", HttpStatus.FORBIDDEN),
    APPOINTMENT_NOT_PENDING(1030, "Chỉ có thể chỉnh sửa lịch hẹn đang ở trạng thái Chờ xác nhận", HttpStatus.BAD_REQUEST),
    APPOINTMENT_ALREADY_CANCELLED_OR_DONE(1031, "Lịch hẹn đã hoàn thành hoặc đã hủy, không thể hủy lại", HttpStatus.BAD_REQUEST),
    APPOINTMENT_NOT_IN_PROGRESS(1032, "Chỉ có thể lập nhật ký trị liệu khi lịch hẹn đang ở trạng thái Đang trị liệu", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
