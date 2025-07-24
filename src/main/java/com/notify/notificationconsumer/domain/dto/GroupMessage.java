package com.notify.notificationconsumer.domain.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessage {
    private Long messageId;
    private String content;
    private String groupId;
    private String groupTitle;
    private List<Long> userIds;
}