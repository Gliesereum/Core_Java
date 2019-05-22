package com.gliesereum.karma.model.entity.chat;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "chat_message")
public class ChatMessageEntity extends DefaultEntity {

    @Column(name = "chat_id")
    private UUID chatId;

    @Column(name = "message")
    private String message;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}