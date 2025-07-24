package com.notify.notificationconsumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notify.notificationconsumer.domain.model.MessageContent;


public interface MessageContentRepository extends JpaRepository<MessageContent, Long> {
    Optional<MessageContent> findById(Long id);
}
