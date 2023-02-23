package com.disl.starter.features.notification.model;

import com.disl.starter.features.notification.enums.NotificationType;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

public class SendNotificationRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String message;

    private NotificationType notificationType;

    private Set<Long> recipientIds = new HashSet<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Long> getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(Set<Long> recipientIds) {
        this.recipientIds = recipientIds;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
