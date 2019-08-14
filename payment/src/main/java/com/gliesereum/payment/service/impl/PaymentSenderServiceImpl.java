package com.gliesereum.payment.service.impl;

import com.gliesereum.payment.model.entity.PaymentSenderEntity;
import com.gliesereum.payment.model.repository.jpa.PaymentSenderRepository;
import com.gliesereum.payment.service.PaymentSenderService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.payment.PaymentSenderDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.FAVORITE_EXIST_IN_OBJECT;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.OBJECT_ID_EMPTY;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PaymentSenderServiceImpl extends DefaultServiceImpl<PaymentSenderDto, PaymentSenderEntity> implements PaymentSenderService {

    private static final Class<PaymentSenderDto> DTO_CLASS = PaymentSenderDto.class;
    private static final Class<PaymentSenderEntity> ENTITY_CLASS = PaymentSenderEntity.class;

    @Autowired
    public PaymentSenderServiceImpl(PaymentSenderRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    private final PaymentSenderRepository repository;

    @Override
    @Transactional
    public PaymentSenderDto create(PaymentSenderDto dto) {
        return super.create(dto);
    }

    @Override
    @Transactional
    public PaymentSenderDto update(PaymentSenderDto dto) {
        checkModel(dto);
        if(dto.isFavorite()){
            PaymentSenderEntity favorite = null;//repository.getByObjectIdAndFavorite(dto.getObjectId(),true);
            if(favorite != null && !favorite.getId().equals(dto.getId())){
               throw new ClientException(FAVORITE_EXIST_IN_OBJECT);
            }
        }
        return super.update(dto);
    }

    @Override
    public PaymentSenderDto getById(UUID id) {
        PaymentSenderDto result = null;
        if (id != null) {
            result = super.getById(id);
        }
        return result;
    }

    private void checkModel(PaymentSenderDto dto) {
        if (dto == null)
            throw new ClientException(BODY_INVALID);
        if (dto.getObjectId() == null)
            throw new ClientException(OBJECT_ID_EMPTY);
    }
}
