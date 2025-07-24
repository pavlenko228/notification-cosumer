package com.notify.notificationconsumer.controller;

import java.time.Duration;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.notify.notificationconsumer.domain.dto.ReadConfirmRequest;
import com.notify.notificationconsumer.domain.dto.RegisterFcmTokenRequest;
import com.notify.notificationconsumer.domain.model.User;
import com.notify.notificationconsumer.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController("/api/notify-consumer")
@RequiredArgsConstructor
public class NotifyController {
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;


    @PostMapping("/register-token")
    public ResponseEntity<Void> registerFcmToken(
        @RequestParam RegisterFcmTokenRequest request) {
        userRepository.save(new User(request.getId(), request.getFcmToken(), null));
        return ResponseEntity.ok().build();
    }


    @PostMapping("/read-status")
    public ResponseEntity<Void> confirmRead(@RequestBody ReadConfirmRequest request) {
        String key = "read:" + request.getMessageId() + ":" + request.getUserId();
        redisTemplate.opsForValue().set(key, "1", Duration.ofDays(30));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-interval")
    public ResponseEntity<Void> updateResendInterval(
        @RequestParam Long userId,
        @RequestParam Integer intervalMinutes) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setIntervalMinutes(intervalMinutes);
            userRepository.save(user);
        });
        return ResponseEntity.ok().build();
    }
}
