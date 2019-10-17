package com.gliesereum.notification.bot.impl;

import com.gliesereum.notification.bot.NotificationTelegramBotService;
import com.gliesereum.notification.service.telegram.TelegramChatActionService;
import com.gliesereum.notification.service.telegram.TelegramChatService;
import com.gliesereum.share.common.exception.rest.RestRequestException;
import com.gliesereum.share.common.exchange.service.account.UserAuthExchangeService;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.exchange.service.account.UserPhoneExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordNotificationDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.TelegramAction;
import com.gliesereum.share.common.model.dto.notification.telegram.TelegramChatActionDto;
import com.gliesereum.share.common.model.dto.notification.telegram.TelegramChatDto;
import com.gliesereum.share.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class NotificationTelegramBotServiceImpl extends TelegramLongPollingBot implements NotificationTelegramBotService {
	
	@Value("${telegram.bot.token}")
	private String telegramBotToken;
	
	@Value("${telegram.bot.user-name}")
	private String telegramBotUserName;
	
	@Autowired
	private TelegramChatService telegramChatService;
	
	@Autowired
	private TelegramChatActionService telegramChatActionService;
	
	@Autowired
	private UserPhoneExchangeService userPhoneExchangeService;
	
	@Autowired
	private UserAuthExchangeService userAuthExchangeService;
	
	@Autowired
	private UserExchangeService userExchangeService;
	
	@Override
	public void recordCreateNotification(RecordNotificationDto data) {
		if (data != null) {
			BaseRecordDto record = data.getRecord();
			BaseBusinessDto business = data.getBusiness();
			String text = 
					"*Поступил новый заказа на бизнес:* " + business.getName() + "\n"
					+ "*Дата:* " + record.getBegin().toLocalDate().toString() + "\n"
					+ "*Время:* " + record.getBegin().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")) + "\n"
					+ "*Номер телефона:* " + record.getClient().getPhone() + "\n";
					
			if (StringUtils.isNotBlank(data.getRecord().getDescription())) {
				text = text + "*Комментарий:*" + record.getDescription();
			}
			
			List<TelegramChatDto> users = telegramChatService.getByUserIds(data.getUserIds());
			if (CollectionUtils.isNotEmpty(users)) {
				for (TelegramChatDto chat : users) {
					try {
						SendMessage sendMessage = new SendMessage();
						sendMessage.setText(text);
						send(sendMessage, null, chat.getChatId(), false);
						
					} catch (Exception e) {
						log.error("While send message", e);
					}
				}
				
			}
		}
	}
	
	@Override
	public void onUpdateReceived(Update update) {
		
		CallbackQuery callbackQuery = update.getCallbackQuery();
		if (callbackQuery != null) {
			Message message = callbackQuery.getMessage();
			switch (callbackQuery.getData()) {
				case "setphone": {
					processSetPhone(message, true);
					return;
				}
				case "cancelaction": {
					cancelAction(message, true);
					return;
				}
			}
			
		} else {
			Message message = update.getMessage();
			if ((message != null) && (message.getText() != null)) {
				switch (message.getText()) {
					case "/start": {
						processStart(message);
						return;
					}
					case "/menu": {
						processMenu(message);
						return;
					}
				}
			}
		}
		Message message = update.getMessage();
		TelegramChatActionDto action = telegramChatActionService.getByChatId(message.getChatId());
		if (action != null) {
			processAction(action, message);
		}
	}
	
	@Override
	public String getBotUsername() {
		return telegramBotUserName;
	}
	
	@Override
	public String getBotToken() {
		return telegramBotToken;
	}
	
	private void processAction(TelegramChatActionDto telegramAction, Message message) {
		switch (telegramAction.getAction()) {
			case ADD_PHONE: {
				processActionAddPhone(message);
				break;
			}
			case VERIFY_PHONE: {
				processActionVerifyPhone(telegramAction, message);
				break;
			}
		}
	}
	
	private void processActionVerifyPhone(TelegramChatActionDto action, Message message) {
		String code = message.getText();
		String phone = action.getValue();
		try {
			AuthDto auth = userAuthExchangeService.auth(phone, code);
			TelegramChatDto telegramChatDto = telegramChatService.create(message.getChatId(), auth.getUser().getId());
			
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Номер телефона успешно добавлен: " + phone);
			send(sendMessage, message, message.getChatId(), false);
			telegramChatActionService.deleteByChatId(message.getChatId());
			
		} catch (RestRequestException ex) {
			SendMessage sendMessage = new SendMessage();
			switch (ex.getErrorCode()) {
				case 1164: {
					sendMessage.setText("Неверный код или время истекло");
					break;
				}
				default: {
					sendMessage.setText("Ошибка");
					break;
				}
			}
			InlineKeyboardButton cancelButton = new InlineKeyboardButton();
			cancelButton.setText("Отмена");
			cancelButton.setCallbackData("cancelaction");
			InlineKeyboardButton addWorkerButton = new InlineKeyboardButton();
			addWorkerButton.setText("Еще раз");
			addWorkerButton.setCallbackData("setphone");
			
			InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
			inlineKeyboardMarkup.setKeyboard(List.of(List.of(addWorkerButton, cancelButton)));
			
			sendMessage.setReplyMarkup(inlineKeyboardMarkup);
			send(sendMessage, message, message.getChatId(), false);
		}
		
	}
	
	private void processActionAddPhone(Message message) {
		String phone;
		Contact contact = message.getContact();
		if (contact == null) {
			phone = message.getText();
		} else {
			phone = contact.getPhoneNumber();
			if (message.getFrom().getId().equals(contact.getUserID())) {
				PublicUserDto publicUserDto = new PublicUserDto();
				publicUserDto.setPhone(phone);
				publicUserDto = userExchangeService.createOrGetPublicUser(publicUserDto);
				TelegramChatDto telegramChatDto = telegramChatService.create(message.getChatId(), publicUserDto.getId());
				
				SendMessage sendMessage = new SendMessage();
				sendMessage.setText("Номер телефона успешно добавлен: " + phone);
				send(sendMessage, message, message.getChatId(), false);
				telegramChatActionService.deleteByChatId(message.getChatId());
				return;
			} 
		}
		if (!RegexUtil.phoneIsValid(phone)) {
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Неверный номер телефона: " + phone + ", попробуйте снова");
			addCancelMenu(sendMessage);
			
			send(sendMessage, message, message.getChatId(), false);
		} else {
			try {
				userPhoneExchangeService.sendCode(phone);
			} catch (Exception e) {
				SendMessage sendMessage = new SendMessage();
				sendMessage.setText("Неверный номер телефона: " + phone + ", попробуйте снова");
				addCancelMenu(sendMessage);
				
				send(sendMessage, message, message.getChatId(), false);
			}
			
			telegramChatActionService.create(message.getChatId(), TelegramAction.VERIFY_PHONE, phone);
			
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("На номер: " + phone + " отправленна смс с кодом, введите ее");
			addCancelMenu(sendMessage);
			
			
			send(sendMessage, message, message.getChatId(), false);
		}
	}
	
	private void cancelAction(Message message, boolean isButton) {
		telegramChatActionService.deleteByChatId(message.getChatId());
		
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("Действие отменено");
		addMainMenu(sendMessage);
		
		send(sendMessage, message, message.getChatId(), isButton);
	}
	
	
	private void processSetPhone(Message originalMessage, boolean isButton) {
		if (telegramChatService.existByChatId(originalMessage.getChatId())) {
			SendMessage sendMessage = new SendMessage();
			sendMessage.setText("Данный чат уже настроен, ожидайте поступления заказов");
			
			send(sendMessage, originalMessage, originalMessage.getChatId(), isButton);
		} else {
			SendMessage sendMessage = new SendMessage();
			String text = "";
			text = text + "Введите номер телефона";
			if (originalMessage.getChat().isUserChat()) {
				text = text + " или отправте свой текущий номер";
				KeyboardButton keyboardButton = new KeyboardButton();
				keyboardButton.setRequestContact(true);
				keyboardButton.setText("Отправить мой номер");
				KeyboardRow row = new KeyboardRow();
				row.add(keyboardButton);
				ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
				replyKeyboardMarkup.setKeyboard(List.of(row));
				replyKeyboardMarkup.setOneTimeKeyboard(true);
				replyKeyboardMarkup.setResizeKeyboard(true);
				sendMessage.setReplyMarkup(replyKeyboardMarkup);
			}
			sendMessage.setText(text);
			telegramChatActionService.create(originalMessage.getChatId(), TelegramAction.ADD_PHONE);
			send(sendMessage, originalMessage, originalMessage.getChatId(), isButton);
		}
		
	}
	
	private void processStart(Message originalMessage) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("Добро пожаловать. \nДля начала работы добавте номер телефона сотрудника с помошью команды 'Указать номер телефона'");
		addMainMenu(sendMessage);
		
		send(sendMessage, originalMessage, originalMessage.getChatId(), false);
	}
	
	private void processMenu(Message originalMessage) {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText("Выберите один из пунктов");
		addMainMenu(sendMessage);
		
		send(sendMessage, originalMessage, originalMessage.getChatId(), false);
	}
	
	
	private void send(SendMessage sendMessage, Message originalMessage, Long chatId, boolean isButton) {
		sendMessage.enableMarkdown(true);
		sendMessage.setChatId(chatId);
		if ((originalMessage != null) && !isButton) {
			sendMessage.setReplyToMessageId(originalMessage.getMessageId());
		}
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			log.warn("Error while send", e);
		}
	}
	
	private void addMainMenu(SendMessage message) {
		InlineKeyboardButton addWorkerButton = new InlineKeyboardButton();
		addWorkerButton.setText("Указать номер телефона");
		addWorkerButton.setCallbackData("setphone");
		
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(List.of(List.of(addWorkerButton)));
		
		message.setReplyMarkup(inlineKeyboardMarkup);
	}
	
	private void addCancelMenu(SendMessage message) {
		InlineKeyboardButton cancelButton = new InlineKeyboardButton();
		cancelButton.setText("Отмена");
		cancelButton.setCallbackData("cancelaction");
		
		InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
		inlineKeyboardMarkup.setKeyboard(List.of(List.of(cancelButton)));
		
		message.setReplyMarkup(inlineKeyboardMarkup);
	}
}
