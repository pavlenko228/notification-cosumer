package com.notify.notificationconsumer.domain.model;



import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "message_content_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageContent {

    @Id
    private Long id;

    private String content;

    private String groupTitle;

}
