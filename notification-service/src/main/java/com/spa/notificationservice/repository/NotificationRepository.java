package com.spa.notificationservice.repository;

import com.spa.notificationservice.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(String recipientId);

    long countByRecipientIdAndReadFalse(String recipientId);
}
