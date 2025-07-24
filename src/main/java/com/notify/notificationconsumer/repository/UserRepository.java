package com.notify.notificationconsumer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.notify.notificationconsumer.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
}