package com.notify.notificationconsumer.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.notify.notificationconsumer.domain.dto.GroupMessage;
import com.notify.notificationconsumer.domain.dto.PushInfo;
import com.notify.notificationconsumer.domain.model.MessageContent;
import com.notify.notificationconsumer.repository.MessageContentRepository;
import com.notify.notificationconsumer.repository.UserRepository;
import com.notify.notificationconsumer.service.contract.CacheService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class MainServiceImpl {

    private final CacheService cacheService;
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final MessageContentRepository messageContentRepository;

    @Value("${app.resend.max-retention-days}")
    private long maxRetentionDays;

    @Value("${app.resend.default-interval-min}")
    private long defaultResendIntervalMinutes;

    //broker
    @KafkaListener(topics = "notifications.medium")
    public void handleMediumPriority(GroupMessage groupMessage) {

        groupMessage.getUserIds().forEach(userId -> {
            if (!cacheService.isMessageRead(userId, groupMessage.getMessageId())) {
                PushInfo pushInfo = PushInfo.builder()
                    .groupTitle(groupMessage.getGroupTitle())
                    .content(groupMessage.getContent())
                    .build();
                sendPushToDevice(userId, pushInfo);
            }
        });
    }

    @KafkaListener(topics = "notifications.high")
    public void handleHighPriority(GroupMessage groupMessage) {

        groupMessage.getUserIds().forEach(userId -> {
            if (!cacheService.isMessageRead(userId, groupMessage.getMessageId())) {
                PushInfo pushInfo = PushInfo.builder()
                    .groupTitle(groupMessage.getGroupTitle())
                    .content(groupMessage.getContent())
                    .build();
                sendPushToDevice(userId, pushInfo);
                messageContentRepository.save(new MessageContent(
                    groupMessage.getMessageId(),
                    groupMessage.getContent(),
                    groupMessage.getGroupTitle()));
                cacheService.markMessageUnreadable(userId, groupMessage.getMessageId());
            }
        });
    }

    //fierbase
    public void sendPushToDevice(Long userId, PushInfo pushInfo) {
        userRepository.findById(userId).ifPresent(user -> {
            Message message = Message.builder()
                        .setNotification(Notification.builder()
                            .setTitle(pushInfo.getGroupTitle())
                            .setBody(pushInfo.getContent())
                            .build())
                        .setToken(user.getFcmToken())
                        .build();
            try {
                firebaseMessaging.send(message);
            } catch (FirebaseMessagingException ignored) {}
        });
    }

    @Scheduled(fixedRateString = "${app.resend.check-delay-min}")
    public void resendHighPriorityNotifications() {
        ScanOptions options = ScanOptions.scanOptions()
            .match("resend:*")
            .count(100)
            .build();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            while (cursor.hasNext()) {
                processResendKey(cursor.next());
            }
        }
    }

    public void processResendKey(String key) {
        String[] parts = key.split(":");
        if (parts.length != 3) return;

        try {
            Long userId = Long.parseLong(parts[1]);
            Long messageId = Long.parseLong(parts[2]);
            
            if (!cacheService.isMessageRead(userId, messageId)) {
                checkAndResendNotification(key, userId, messageId);
            } else {
                redisTemplate.delete(key);
            }
        } catch (IllegalArgumentException e) {
            redisTemplate.delete(key);
        }
    }

    public void checkAndResendNotification(String key, Long userId, Long messageId) {
        userRepository.findById(userId).ifPresent(user -> {
            String lastSentStr = redisTemplate.opsForValue().get(key);
            long currentTime = System.currentTimeMillis();
            long resendIntervalMillis = TimeUnit.MINUTES.toMillis(
                user.getIntervalMinutes() != null ? 
                user.getIntervalMinutes() : defaultResendIntervalMinutes
            );

            if (lastSentStr == null || 
                currentTime - Long.parseLong(lastSentStr) >= resendIntervalMillis) {
                
                messageContentRepository.findById(messageId).ifPresent(message -> {
                    PushInfo pushInfo = PushInfo.builder()
                        .content(message.getContent())
                        .groupTitle(message.getGroupTitle())
                        .build();
                    sendPushToDevice(userId, pushInfo);
                    redisTemplate.opsForValue().set(
                        key, 
                        String.valueOf(currentTime),
                        maxRetentionDays, 
                        TimeUnit.DAYS
                    );
                });
            }
        });
    }
    
}


