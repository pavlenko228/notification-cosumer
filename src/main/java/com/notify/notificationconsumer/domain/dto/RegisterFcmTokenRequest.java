package com.notify.notificationconsumer.domain.dto;

import com.google.firebase.database.annotations.NotNull;

import lombok.Data;

@Data
public class RegisterFcmTokenRequest {
    @NotNull
    private Long id;

    @NotNull
    private String fcmToken;

    @NotNull
    private Integer intervalMinutes;
}
