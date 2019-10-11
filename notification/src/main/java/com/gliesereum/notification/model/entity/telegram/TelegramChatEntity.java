package com.gliesereum.notification.model.entity.telegram;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "telegram_chat")
public class TelegramChatEntity extends DefaultEntity {
	
	@Column(name = "chat_id")
	private Long chatId;
	
	@Column(name = "user_id")
	private UUID userId;
	
}
