package com.spa.notificationservice.dto.response;

import com.spa.notificationservice.entity.NotificationChannel;
import com.spa.notificationservice.entity.NotificationStatus;
import com.spa.notificationservice.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {
    private String id;
    private String recipientId;
    private NotificationType type;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String title;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
}
