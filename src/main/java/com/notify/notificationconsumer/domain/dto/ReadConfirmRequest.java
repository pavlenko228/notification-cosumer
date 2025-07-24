package com.notify.notificationconsumer.domain.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReadConfirmRequest {
    @NotNull
    private UUID messageId;
    
    @NotNull
    private Long userId;
}
