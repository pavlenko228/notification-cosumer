package com.notify.notificationconsumer.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String fcmToken;

    @Column(nullable = false)
    private Integer intervalMinutes;
}