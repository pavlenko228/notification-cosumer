package com.notify.notificationconsumer.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.notify.notificationconsumer.service.contract.CacheService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService{

    private final StringRedisTemplate redisTemplate;

    @Value("${app.resend.max-retention-days}")
    private long maxRetentionDays;

    public boolean isMessageRead(Long userId, Long messageId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(
            "read:" + userId + ":" + messageId
        ));
    }


    public void markMessageUnreadable(Long userId, Long messageId) {
        redisTemplate.opsForValue().set(
            "resend:" + userId + ":" + messageId,
            String.valueOf(System.currentTimeMillis()),
            maxRetentionDays,
            TimeUnit.DAYS
            );
    }
}
