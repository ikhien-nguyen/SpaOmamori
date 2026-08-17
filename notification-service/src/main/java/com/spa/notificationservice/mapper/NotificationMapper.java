package com.spa.notificationservice.mapper;

import com.spa.notificationservice.dto.response.NotificationResponse;
import com.spa.notificationservice.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
