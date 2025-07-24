package com.notify.notificationconsumer.service.contract;

public interface CacheService {

    boolean isMessageRead(Long userId, Long messageId);

    void markMessageUnreadable(Long userId, Long messageId);


}
