package com.gliesereum.mail.service.telegram.impl;

import com.gliesereum.mail.service.telegram.FeedBackTelegramBotService;
import com.gliesereum.mail.service.telegram.TelegramBotService;
import com.gliesereum.share.common.model.dto.mail.TelegramBotDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class FeedBackTelegramBotServiceImpl extends TelegramLongPollingBot implements FeedBackTelegramBotService {

    @Value("${telegram.bot.feed-back.token}")
    private String feedBackBotToken;

    @Value("${telegram.bot.feed-back.user-name}")
    private String feedBackBotUserName;

    @Value("${telegram.bot.feed-back.message.start}")
    private String feedBackBotStartMessage;

    @Value("${telegram.bot.feed-back.message.end}")
    private String feedBackBotEndMessage;

    @Value("${telegram.bot.feed-back.message.help}")
    private String feedBackBotHelpMessage;

    private final String START = "/start";

    private final String END = "/end";

    private final String HELP = "/help";

    @Autowired
    private TelegramBotService telegramBotService;

    @Override
    public String getBotUsername() {
        return feedBackBotUserName;
    }

    @Override
    public String getBotToken() {
        return feedBackBotToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message != null && message.hasText()) {

            if (message.getText().equals(START)) {
                String answer = "";
                List<TelegramBotDto> telegramBots = telegramBotService.getByActive(true);
                if (CollectionUtils.isNotEmpty(telegramBots)) {
                    TelegramBotDto existChatBot = telegramBots.get(0);
                    if (!existChatBot.getChatId().equals(message.getChatId())) {
                        answer = "You can’t add this bot while it's working in another chat";
                    } else {
                        answer = "This bot already working in this chat. All commands: /help";
                    }
                    sendMsg(message, answer);
                } else {
                    if (message.getChat() != null) {
                        String chatName = message.getChat().getTitle();
                        TelegramBotDto telegramBot = new TelegramBotDto();
                        telegramBot.setActive(true);
                        telegramBot.setChatId(message.getChatId());
                        telegramBot.setChatName(chatName);
                        telegramBotService.create(telegramBot);
                        answer = feedBackBotStartMessage.concat(chatName);
                        sendMsg(message, answer);
                    }
                }
            }
            if (message.getText().equals(END)) {
                TelegramBotDto existChatBot = telegramBotService.getByChatId(message.getChatId());
                if (existChatBot != null) {
                    telegramBotService.delete(existChatBot.getId());
                    sendMsg(message, feedBackBotEndMessage.concat(existChatBot.getChatName()));
                }
            }
            if (message.getText().equals(HELP)) {
                sendMsg(message, feedBackBotHelpMessage);
            }
        }
    }

    @Override
    public void sendMessageToChatBot(String text) {
        List<TelegramBotDto> telegramBots = telegramBotService.getByActive(true);
        if (CollectionUtils.isNotEmpty(telegramBots)) {
            TelegramBotDto telegramBot = telegramBots.get(0);
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(telegramBot.getChatId().toString());
            sendMessage.setText(text);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
