package com.spa.notificationservice.dto.request;

import com.spa.notificationservice.entity.NotificationChannel;
import com.spa.notificationservice.entity.NotificationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Dung boi cac service KHAC (appointment-service, payment-service...) goi
 * sang qua Feign de tao + gui thong bao - khong phai API public cho FE goi
 * truc tiep, nen khong lay recipientId tu JWT ma nhan thang trong body (noi
 * goi da biet ro dang tao thong bao cho ai).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {

    @NotBlank(message = "recipientId không được để trống")
    private String recipientId;

    // Bat buoc neu channel = EMAIL, bo qua neu channel = IN_APP.
    @Email(message = "Email không đúng định dạng")
    private String recipientEmail;

    @NotNull(message = "Loại thông báo không được để trống")
    private NotificationType type;

    @NotNull(message = "Kênh gửi không được để trống")
    private NotificationChannel channel;

    @NotBlank(message = "Tiêu đề không được để trống")
    private String title;

    @NotBlank(message = "Nội dung không được để trống")
    private String content;
}
