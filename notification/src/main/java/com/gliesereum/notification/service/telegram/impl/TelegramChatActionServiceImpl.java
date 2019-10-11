package com.gliesereum.notification.service.telegram.impl;

import com.gliesereum.notification.model.entity.telegram.TelegramChatActionEntity;
import com.gliesereum.notification.model.repository.jpa.telegram.TelegramChatActionRepository;
import com.gliesereum.notification.service.telegram.TelegramChatActionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.notification.enumerated.TelegramAction;
import com.gliesereum.share.common.model.dto.notification.telegram.TelegramChatActionDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class TelegramChatActionServiceImpl extends DefaultServiceImpl<TelegramChatActionDto, TelegramChatActionEntity> implements TelegramChatActionService {
	
	private static final Class<TelegramChatActionDto> DTO_CLASS = TelegramChatActionDto.class;
	private static final Class<TelegramChatActionEntity> ENTITY_CLASS = TelegramChatActionEntity.class;
	
	private final TelegramChatActionRepository telegramChatActionRepository;
	
	@Autowired
	public TelegramChatActionServiceImpl(TelegramChatActionRepository telegramChatActionRepository, DefaultConverter defaultConverter) {
		super(telegramChatActionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
		this.telegramChatActionRepository = telegramChatActionRepository;
	}
	
	@Override
	public TelegramChatActionDto getByChatId(Long chatId) {
		TelegramChatActionDto result = null;
		if (chatId != null) {
			TelegramChatActionEntity entity = telegramChatActionRepository.findByChatId(chatId);
			result = converter.convert(entity, dtoClass);
		}
		return result;
	}
	
	@Override
	public TelegramChatActionDto create(Long chatId, TelegramAction action) {
		TelegramChatActionDto result = null;
		if (ObjectUtils.allNotNull(chatId, action)) {
			result = getByChatId(chatId);
			if (result == null) {
				result = new TelegramChatActionDto();
			}
			result.setChatId(chatId);
			result.setAction(action);
			TelegramChatActionEntity entity = converter.convert(result, entityClass);
			entity = telegramChatActionRepository.saveAndFlush(entity);
			result = converter.convert(entity, dtoClass);
		}
		return result;
	}
	
	@Override
	public TelegramChatActionDto create(Long chatId, TelegramAction action, String value) {
		TelegramChatActionDto result = null;
		if (ObjectUtils.allNotNull(chatId, action)) {
			result = getByChatId(chatId);
			if (result == null) {
				result = new TelegramChatActionDto();
			}
			result.setChatId(chatId);
			result.setAction(action);
			result.setValue(value);
			TelegramChatActionEntity entity = converter.convert(result, entityClass);
			entity = telegramChatActionRepository.saveAndFlush(entity);
			result = converter.convert(entity, dtoClass);
		}
		return result;
	}
	
	@Override
	@Transactional
	public void deleteByChatId(Long chatId) {
		if (chatId != null) {
			telegramChatActionRepository.deleteAllByChatId(chatId);
		}
	}
}
