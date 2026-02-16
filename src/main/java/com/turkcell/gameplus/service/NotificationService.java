package com.turkcell.gameplus.service;

import com.turkcell.gameplus.model.Notification;
import com.turkcell.gameplus.model.User;
import com.turkcell.gameplus.repository.NotificationRepository;
import com.turkcell.gameplus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public Notification sendNotification(String userId, String message, java.time.LocalDate date) {
        log.debug("Sending notification to user: {} for date: {}", userId, date);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        Notification notification = new Notification();
        notification.setNotificationId("N-" + UUID.randomUUID().toString());
        notification.setUser(user);
        notification.setUserId(userId);
        notification.setChannel("BiP");
        notification.setMessage(message);
        notification.setSentAt(date.atTime(java.time.LocalTime.now()));

        Notification savedNotification = notificationRepository.save(notification);
        log.debug("Notification sent: {}", savedNotification.getNotificationId());

        return savedNotification;
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdOrderBySentAtDesc(userId);
    }

}

