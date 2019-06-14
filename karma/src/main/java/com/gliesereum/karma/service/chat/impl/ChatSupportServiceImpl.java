package com.gliesereum.karma.service.chat.impl;

import com.gliesereum.karma.model.entity.chat.ChatSupportEntity;
import com.gliesereum.karma.model.repository.jpa.chat.ChatSupportRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.chat.ChatSupportService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.chat.ChatSupportDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ChatSupportServiceImpl extends DefaultServiceImpl<ChatSupportDto, ChatSupportEntity> implements ChatSupportService {

    private static final Class<ChatSupportDto> DTO_CLASS = ChatSupportDto.class;
    private static final Class<ChatSupportEntity> ENTITY_CLASS = ChatSupportEntity.class;

    private final ChatSupportRepository chatSupportRepository;

    @Autowired
    private BaseBusinessService businessService;

    @Autowired
    public ChatSupportServiceImpl(ChatSupportRepository chatSupportRepository, DefaultConverter defaultConverter) {
        super(chatSupportRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.chatSupportRepository = chatSupportRepository;
    }

    @Override
    @Transactional
    public ChatSupportDto create(ChatSupportDto dto) {
        ChatSupportDto result = null;
        checkPermission(dto);
        if (dto != null && !chatSupportRepository.existsByBusinessIdAndUserId(dto.getBusinessId(), dto.getUserId())) {
            result = super.create(dto);
        }
        return result;
    }

    @Override
    @Transactional
    public ChatSupportDto update(ChatSupportDto dto) {
        checkPermission(dto);
        return super.update(dto);
    }

    @Override
    @Transactional
    public List<ChatSupportDto> create(List<ChatSupportDto> dtos) {
        if (CollectionUtils.isNotEmpty(dtos)) {
            ChatSupportDto support = dtos.get(0);
            if (dtos.stream().anyMatch(i -> !i.getBusinessId().equals(support.getBusinessId()))) {
                throw new ClientException(ALL_OBJECT_ID_NOT_EQUALS);
            }
            checkPermission(support);
            dtos = dtos.stream().filter(f -> !chatSupportRepository.existsByBusinessIdAndUserId(f.getBusinessId(), f.getUserId())).collect(Collectors.toList());
        }
        return super.update(dtos);
    }

    @Override
    public List<ChatSupportDto> getByBusinessId(UUID businessId) {
        List<ChatSupportEntity> entities = chatSupportRepository.getAllByBusinessId(businessId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public boolean existChatSupport(UUID userId, UUID businessId) {
        return chatSupportRepository.existsByBusinessIdAndUserId(businessId, userId);
    }

    @Transactional
    public void delete(UUID id) {
        ChatSupportDto dto = getById(id);
        if (dto != null) {
            checkPermission(dto);
            super.delete(id);
        }
    }

    private void checkPermission(ChatSupportDto dto) {
        if (dto == null || dto.getBusinessId() == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        if (!businessService.currentUserHavePermissionToActionInBusinessLikeOwner(dto.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }
}
