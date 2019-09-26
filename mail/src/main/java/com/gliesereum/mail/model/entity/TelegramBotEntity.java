package com.gliesereum.mail.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "telegram_bot")
public class TelegramBotEntity extends DefaultEntity {

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name")
    private String chatName;

    @Column(name = "active")
    private Boolean active;

}