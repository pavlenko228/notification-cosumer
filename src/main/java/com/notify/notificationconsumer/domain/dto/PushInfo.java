package com.notify.notificationconsumer.domain.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PushInfo {
    private String groupTitle;
    private String content;
}
