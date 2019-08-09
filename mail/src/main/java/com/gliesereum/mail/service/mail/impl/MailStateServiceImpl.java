package com.gliesereum.mail.service.mail.impl;

import com.gliesereum.mail.model.entity.MailStateEntity;
import com.gliesereum.mail.model.repository.jpa.MailStateRepository;
import com.gliesereum.mail.service.mail.MailStateService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class MailStateServiceImpl extends DefaultServiceImpl<MailStateDto, MailStateEntity> implements MailStateService {

    private static final Class<MailStateDto> DTO_CLASS = MailStateDto.class;
    private static final Class<MailStateEntity> ENTITY_CLASS = MailStateEntity.class;

    private MailStateRepository mailStateRepository;

    @Autowired
    public MailStateServiceImpl(MailStateRepository mailStateRepository, DefaultConverter defaultConverter) {
        super(mailStateRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.mailStateRepository = mailStateRepository;
    }

    @Override
    public List<MailStateDto> getByMessageStatusAndDateAfter(String status, LocalDateTime date) {
        List<MailStateEntity> entities = mailStateRepository.findAllByMessageStatusAndCreateAfter(status, date);
        return converter.convert(entities, dtoClass);
    }
}
