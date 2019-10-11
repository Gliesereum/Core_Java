package com.gliesereum.notification.model.entity.telegram;

import com.gliesereum.share.common.model.dto.notification.enumerated.TelegramAction;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "telegram_chat_action")
public class TelegramChatActionEntity extends DefaultEntity {
	
	@Column(name = "chat_id")
	private Long chatId;
	
	@Column(name = "action")
	@Enumerated(EnumType.STRING)
	private TelegramAction action;
	
	@Column(name = "value")
	private String value;
}
