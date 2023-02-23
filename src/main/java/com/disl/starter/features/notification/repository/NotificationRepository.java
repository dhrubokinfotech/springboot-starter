package com.disl.starter.features.notification.repository;

import com.disl.starter.entities.User;
import com.disl.starter.features.notification.entity.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient = ?1 AND n.isRead = false")
    int getUnreadCounter(User recipient);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.recipient = :recipient AND n.isRead = false")
    int markNotificationAsRead(@Param("recipient") User recipient);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipient.id = :recipientId AND n.isRead = false")
    int getUnreadCounterByRecipientId(@Param("recipientId") long recipientId);

    Page<Notification> findAllByRecipient(User recipient, Pageable pageable);

    @Transactional
    void deleteAllBySender_IdOrRecipient_Id(long senderId, long recipientId);
}
