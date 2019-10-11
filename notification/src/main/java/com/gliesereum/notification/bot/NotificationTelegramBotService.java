package com.gliesereum.notification.bot;

import com.gliesereum.share.common.model.dto.karma.record.RecordNotificationDto;

public interface NotificationTelegramBotService {
	
	void recordCreateNotification(RecordNotificationDto data);
}
