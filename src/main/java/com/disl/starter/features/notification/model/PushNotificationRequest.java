package com.disl.starter.features.notification.model;

import com.disl.starter.features.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PushNotificationRequest {
    private String to;

    @JsonProperty("notification")
    private PushNotificationPayload pushNotificationPayload;

    @JsonProperty("data")
    private PushNotificationData pushNotificationData;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public PushNotificationPayload getPushNotificationPayload() {
        return pushNotificationPayload;
    }

    public void setPushNotificationPayload(PushNotificationPayload pushNotificationPayload) {
        this.pushNotificationPayload = pushNotificationPayload;
    }

    public PushNotificationData getPushNotificationData() {
        return pushNotificationData;
    }

    public void setPushNotificationData(PushNotificationData pushNotificationData) {
        this.pushNotificationData = pushNotificationData;
    }

    public static class PushNotificationPayload {
        private String body;
        private String title;
        private final String priority = "high";

        @JsonProperty("content_available")
        private final boolean contentAvailable = true;

        public String getTitle() {
            return title != null ? title : "মদীনার পথে";
        }

        public String getBody() {
            return body;
        }

        public String getPriority() {
            return priority;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public boolean isContentAvailable() {
            return contentAvailable;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class PushNotificationData {
        private String body;
        private String title;
        private NotificationType type;
        private long senderId = 0;
        private long recipientId = 0;
        private int unreadCount=0;
        private long typeId = 0;

        @JsonProperty("click_action")
        private final String clickAction = "FLUTTER_NOTIFICATION_CLICK";

        public long getTypeId() {
            return typeId;
        }

        public void setTypeId(long typeId) {
            this.typeId = typeId;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTitle() {
            return title != null ? title : "মদীনার পথে";
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getClickAction() {
            return clickAction;
        }

        public NotificationType getType() {
            return type;
        }

        public void setType(NotificationType type) {
            this.type = type;
        }

        public long getSenderId() {
            return senderId;
        }

        public void setSenderId(long senderId) {
            this.senderId = senderId;
        }

        public long getRecipientId() {
            return recipientId;
        }

        public void setRecipientId(long recipientId) {
            this.recipientId = recipientId;
        }

        public int getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(int unreadCount) {
            this.unreadCount = unreadCount;
        }
    }
}
