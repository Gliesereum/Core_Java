package com.gliesereum.mail.service.telegram;

import com.gliesereum.mail.model.entity.TelegramBotEntity;
import com.gliesereum.share.common.model.dto.mail.TelegramBotDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

public interface TelegramBotService extends DefaultService<TelegramBotDto, TelegramBotEntity> {

    TelegramBotDto getByChatId(Long chatId);

    List<TelegramBotDto> getByActive(Boolean active);

}