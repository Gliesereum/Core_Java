package com.gliesereum.karma.service.chat.impl;

import com.gliesereum.karma.facade.notification.ChatNotificationFacade;
import com.gliesereum.karma.model.entity.chat.ChatMessageEntity;
import com.gliesereum.karma.model.repository.jpa.chat.ChatMessageRepository;
import com.gliesereum.karma.service.chat.ChatMessageService;
import com.gliesereum.karma.service.chat.ChatService;
import com.gliesereum.karma.service.chat.ChatSupportService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.chat.ChatDto;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.model.dto.karma.chat.ChatSupportDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.CHAT_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.NOT_PERMISSION_TO_CHAT;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ChatMessageServiceImpl extends DefaultServiceImpl<ChatMessageDto, ChatMessageEntity> implements ChatMessageService {

    private static final Class<ChatMessageDto> DTO_CLASS = ChatMessageDto.class;
    private static final Class<ChatMessageEntity> ENTITY_CLASS = ChatMessageEntity.class;

    private final ChatMessageRepository repository;

    @Autowired
    private ChatNotificationFacade chatNotificationFacade;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatSupportService supportService;

    @Override
    public List<ChatMessageDto> getAllByChatId(UUID id) {
        SecurityUtil.checkUserByBanStatus();
        ChatDto chat = chatService.getById(id);
        if (chat == null) {
            throw new ClientException(CHAT_NOT_FOUND);
        }
        if (!chat.getUserId().equals(SecurityUtil.getUserId()) && !currentUserHavePermissionLikeBusinessUser(chat.getBusinessId())) {
            throw new ClientException(NOT_PERMISSION_TO_CHAT);
        }
        List<ChatMessageEntity> entities = repository.getAllByChatId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public ChatMessageDto createByBusinessId(UUID id, String message) {
        SecurityUtil.checkUserByBanStatus();
        UUID userId = SecurityUtil.getUserId();
        ChatDto chat = chatService.getByUserIdAndBusinessId(userId, id);
        ChatMessageDto result = new ChatMessageDto();
        result.setCreateDate(LocalDateTime.now());
        result.setMessage(message);
        if (chat != null) {
            result.setChatId(chat.getId());
        } else {
            ChatDto newChat = chatService.create(new ChatDto(userId, id));
            result.setChatId(newChat.getId());
        }
        result = create(result);
        chatNotificationFacade.userMessageNotification(result, userId);
        return result;
    }

    @Override
    @Transactional
    public ChatMessageDto createByChatId(UUID id, String message) {
        SecurityUtil.checkUserByBanStatus();
        ChatDto chat = chatService.getById(id);
        if (chat == null) {
            throw new ClientException(CHAT_NOT_FOUND);
        }
        UUID businessId = chat.getBusinessId();
        if (!currentUserHavePermissionLikeBusinessUser(businessId)) {
            throw new ClientException(NOT_PERMISSION_TO_CHAT);
        }
        ChatMessageDto result = create(new ChatMessageDto(id, message, LocalDateTime.now()));
        chatNotificationFacade.businessMessageNotification(result, businessId);
        return result;
    }

    private boolean currentUserHavePermissionLikeBusinessUser(UUID businessId) {
        List<ChatSupportDto> supports = supportService.getByBusinessId(businessId);
        return CollectionUtils.isNotEmpty(supports) &&
                supports.stream().anyMatch(n -> n.getUserId().equals(SecurityUtil.getUserId()));
    }
}
