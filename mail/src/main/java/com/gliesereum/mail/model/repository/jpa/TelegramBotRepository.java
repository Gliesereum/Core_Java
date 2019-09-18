package com.gliesereum.mail.model.repository.jpa;

import com.gliesereum.mail.model.entity.TelegramBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TelegramBotRepository extends JpaRepository<TelegramBotEntity, UUID> {

    TelegramBotEntity getByChatId(Long chatId);

    List<TelegramBotEntity> getByActive(Boolean active);

}