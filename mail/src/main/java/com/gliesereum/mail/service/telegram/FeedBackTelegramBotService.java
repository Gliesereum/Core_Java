package com.gliesereum.mail.service.telegram;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface FeedBackTelegramBotService {

    void sendMessageToChatBot(String text);

}
