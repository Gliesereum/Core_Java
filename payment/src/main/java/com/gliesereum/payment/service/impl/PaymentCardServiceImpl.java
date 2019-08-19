package com.gliesereum.payment.service.impl;

import com.gliesereum.payment.model.entity.PaymentCardEntity;
import com.gliesereum.payment.model.repository.jpa.PaymentCardRepository;
import com.gliesereum.payment.service.PaymentCardService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.payment.PaymentCardDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.CARD_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.DONT_HAVE_PERMISSION_TO_CARD;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PaymentCardServiceImpl extends DefaultServiceImpl<PaymentCardDto, PaymentCardEntity> implements PaymentCardService {

    private static final Class<PaymentCardDto> DTO_CLASS = PaymentCardDto.class;
    private static final Class<PaymentCardEntity> ENTITY_CLASS = PaymentCardEntity.class;

    @Autowired
    public PaymentCardServiceImpl(PaymentCardRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    private final PaymentCardRepository repository;

    @Transactional
    public void delete(UUID id) {
        checkPermission(id);
        super.delete(id);
    }

    @Override
    public List<PaymentCardDto> getMyCards() {
        SecurityUtil.checkUserByBanStatus();
        List<PaymentCardEntity> entities = repository.getByOwnerId(SecurityUtil.getUserId());
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<PaymentCardDto> makeFavorite(UUID idCard) {
        checkPermission(idCard);
        List<PaymentCardDto> result = getByOwnerId(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(f -> {
                if (f.getId().equals(idCard)) {
                    f.setFavorite(true);
                } else {
                    f.setFavorite(false);
                }
            });
            result = super.update(result);
        }
        return result;
    }

    private List<PaymentCardDto> getByOwnerId(UUID ownerId) {
        List<PaymentCardEntity> entities = repository.getByOwnerId(ownerId);
        return converter.convert(entities, dtoClass);
    }

    private void checkPermission(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        SecurityUtil.checkUserByBanStatus();
        PaymentCardEntity entity = repository.getById(id);
        if (entity == null) {
            throw new ClientException(CARD_NOT_FOUND);
        }
        if (!entity.getOwnerId().equals(SecurityUtil.getUserId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_CARD);
        }

    }
}
