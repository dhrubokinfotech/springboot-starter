package com.disl.starter.features.notification.service;

import com.disl.starter.StarterApplication;
import com.disl.starter.constants.AppUtils;
import com.disl.starter.entities.User;
import com.disl.starter.features.notification.entity.Notification;
import com.disl.starter.features.notification.enums.NotificationType;
import com.disl.starter.features.notification.model.PushNotificationRequest;
import com.disl.starter.features.notification.model.SendNotificationRequest;
import com.disl.starter.features.notification.repository.NotificationRepository;
import com.disl.starter.models.PaginationArgs;
import com.disl.starter.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Value("${fcm.fcmUrl}")
    private String fcmUrl;

    @Value("${fcm.fcmHeaderKey}")
    private String fcmHeaderKey;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    public long getUnreadCounter() {
        return notificationRepository.getUnreadCounter(AppUtils.getLoggedInUser(userService));
    }

    public boolean markNotificationAsRead() {
        return notificationRepository.markNotificationAsRead(AppUtils.getLoggedInUser(userService)) > 0;
    }

    public Page<Notification> getAllNotificationPaginated(PaginationArgs paginationArgs) {
        User recipient = AppUtils.getLoggedInUser(userService);
        Pageable pageable = AppUtils.getPageable(paginationArgs);
        return notificationRepository.findAllByRecipient(recipient, pageable);
    }

    public int getUnreadCounterByRecipient(long recipientId) {
        return notificationRepository.getUnreadCounterByRecipientId(recipientId);
    }

    public void deleteAllBySenderIdOrRecipientId(long userId) {
        notificationRepository.deleteAllBySender_IdOrRecipient_Id(userId, userId);
    }

    @Async
    public void createNotificationAsync(
            User sender, User recipient, String title,
            String message, NotificationType type, long typeId
    ) {
        Notification notification = new Notification();
        notification.setType(type);
        notification.setRead(false);
        notification.setTypeId(typeId);
        notification.setSender(sender);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRecipient(recipient);

        sendPushNotification(notificationRepository.save(notification));
    }

    private void sendPushNotification(Notification notification) {
        if (notification != null) {
            try {
                String title = notification.getTitle();
                String message = notification.getMessage();
                long recipientId = notification.getRecipient().getId();
                PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();

                PushNotificationRequest.PushNotificationData data = new PushNotificationRequest.PushNotificationData();
                data.setTitle(title);
                data.setBody(message);
                data.setRecipientId(recipientId);
                data.setType(notification.getType());
                data.setTypeId(notification.getTypeId());
                data.setSenderId(notification.getSender().getId());
                data.setUnreadCount(getUnreadCounterByRecipient(notification.getRecipient().getId()));

                PushNotificationRequest.PushNotificationPayload payload = new PushNotificationRequest.PushNotificationPayload();
                payload.setTitle(title);
                payload.setBody(message);
                pushNotificationRequest.setPushNotificationData(data);
                pushNotificationRequest.setTo("/topics/" + recipientId);
                pushNotificationRequest.setPushNotificationPayload(payload);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", fcmHeaderKey);

                HttpEntity<PushNotificationRequest> entity = new HttpEntity<>(pushNotificationRequest, headers);
                restTemplate.postForEntity(fcmUrl, entity, String.class);
            } catch (Exception e) {
                e.printStackTrace();
                StarterApplication.logger.info("push notification error: {}", e.getMessage());
            }
        }
    }

    public void createAndSendNotification(SendNotificationRequest request, User sender) {
        List<User> recipients = request.getRecipientIds().stream()
                .map(recipientId -> userService.findByIdWithException(recipientId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for(User user: recipients){
            createNotificationAsync(sender, user, request.getTitle(), request.getMessage(), request.getNotificationType(),0);
        }
    }
}
