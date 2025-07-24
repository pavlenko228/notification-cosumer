package com.notify.notificationconsumer.domain.dto;

import java.time.Instant;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndividualMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFromUniqPairMessageIdAndGroupId;

    private Long userId;

    private Long messageId;
    
    private String groupId;

    private String groupTitle;

    private String content;
    
    private Instant priviousSend;
}
