package com.spa.notificationservice.service;

import com.spa.notificationservice.dto.request.CreateNotificationRequest;
import com.spa.notificationservice.dto.response.NotificationResponse;
import com.spa.notificationservice.entity.Notification;
import com.spa.notificationservice.entity.NotificationChannel;
import com.spa.notificationservice.entity.NotificationStatus;
import com.spa.notificationservice.entity.NotificationType;
import com.spa.notificationservice.exception.AppException;
import com.spa.notificationservice.exception.ErrorCode;
import com.spa.notificationservice.mapper.NotificationMapper;
import com.spa.notificationservice.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {

    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    EmailService emailService;

    /**
     * Tao ban ghi thong bao va gui ngay (dong bo, don gian cho pham vi do an -
     * neu can chiu tai lon hon thi tach buoc gui sang 1 queue/consumer rieng
     * sau nay, controller/entity da san sang cho huong do vi co san status
     * PENDING/SENT/FAILED).
     */
    public NotificationResponse create(CreateNotificationRequest request) {
        if (request.getChannel() == NotificationChannel.EMAIL
                && (request.getRecipientEmail() == null || request.getRecipientEmail().isBlank())) {
            throw new AppException(ErrorCode.EMAIL_REQUIRED_FOR_CHANNEL);
        }

        Notification notification = Notification.builder()
                .recipientId(request.getRecipientId())
                .recipientEmail(request.getRecipientEmail())
                .type(request.getType())
                .channel(request.getChannel())
                .title(request.getTitle())
                .content(request.getContent())
                .status(NotificationStatus.PENDING)
                .read(false)
                .build();

        dispatch(notification);

        notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(notification);
    }

    public List<NotificationResponse> getMyNotifications(String recipientId) {
        return notificationRepository.findByRecipientIdOrderByCreatedAtDesc(recipientId).stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public long countUnread(String recipientId) {
        return notificationRepository.countByRecipientIdAndReadFalse(recipientId);
    }

    public NotificationResponse markAsRead(String id, String recipientId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getRecipientId().equals(recipientId)) {
            throw new AppException(ErrorCode.NOTIFICATION_ACCESS_DENIED);
        }

        notification.setRead(true);
        notificationRepository.save(notification);
        return notificationMapper.toNotificationResponse(notification);
    }

    /**
     * Goi nguoi tao boi cac use case noi bo (VD: InvoicePaidConsumer) khong
     * di qua CreateNotificationRequest - tra ve entity da luu de test/log
     * neu can, khac voi create() la API public tra ve DTO.
     */
    public Notification createInApp(String recipientId, NotificationType type, String title, String content) {
        Notification notification = Notification.builder()
                .recipientId(recipientId)
                .type(type)
                .channel(NotificationChannel.IN_APP)
                .title(title)
                .content(content)
                .status(NotificationStatus.PENDING)
                .read(false)
                .build();

        dispatch(notification);
        return notificationRepository.save(notification);
    }

    private void dispatch(Notification notification) {
        if (notification.getChannel() == NotificationChannel.EMAIL) {
            boolean sent = emailService.send(
                    notification.getRecipientEmail(), notification.getTitle(), notification.getContent());
            notification.setStatus(sent ? NotificationStatus.SENT : NotificationStatus.FAILED);
        } else {
            // IN_APP khong can gui di dau, ban ghi trong DB CHINH LA thong bao.
            notification.setStatus(NotificationStatus.SENT);
        }
        notification.setSentAt(LocalDateTime.now());
    }
}
