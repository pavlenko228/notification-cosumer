package com.notify.notificationconsumer.service.contract;

import com.notify.notificationconsumer.domain.dto.GroupMessage;
import com.notify.notificationconsumer.domain.dto.PushInfo;

public interface MainService {

    void handleMediumPriority(GroupMessage groupMessage);

    void handleHighPriority(GroupMessage groupMessage);

    void sendPushToDevice(Long userId, PushInfo pushInfo);

    void resendHighPriorityNotifications();

    void processResendKey(String key);

    void checkAndResendNotification(String key, Long userId, Long messageId);
}
