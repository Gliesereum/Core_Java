package com.gliesereum.lendinggallery.service.account.impl;

import com.gliesereum.lendinggallery.model.entity.account.MessageEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.account.MessageRepository;
import com.gliesereum.lendinggallery.service.account.MessageService;
import com.gliesereum.lendinggallery.service.customer.CustomerService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.lendinggallery.account.MessageDto;
import com.gliesereum.share.common.model.dto.lendinggallery.customer.CustomerDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SectionType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class MessageServiceImpl extends DefaultServiceImpl<MessageDto, MessageEntity> implements MessageService {

    private static final Class<MessageDto> DTO_CLASS = MessageDto.class;
    private static final Class<MessageEntity> ENTITY_CLASS = MessageEntity.class;

    private final MessageRepository messageRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, DefaultConverter defaultConverter) {
        super(messageRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.messageRepository = messageRepository;
    }

    @Override
    public List<MessageDto> getAllByUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        CustomerDto customer = customerService.findByUserId(SecurityUtil.getUserId());
        List<MessageEntity> entities = null;
        if (customer != null) {
            entities = messageRepository.findAllByCustomerIdOrderByCreate(customer.getId());
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<MessageDto> getAllBySections(List<SectionType> types) {
        List<MessageEntity> entities = messageRepository.findAllBySectionTypeInOrderByCreate(types);
        return converter.convert(entities, dtoClass);
    }
}
