package com.gliesereum.payment.service.wayforpay.impl;

import com.gliesereum.payment.model.entity.wayforpay.WfpCardEntity;
import com.gliesereum.payment.model.repository.jpa.wayforpay.WfpCardRepository;
import com.gliesereum.payment.service.wayforpay.WfpCardService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.payment.payment.UserCardDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpCardDto;
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
public class WfpCardServiceImpl extends DefaultServiceImpl<WfpCardDto, WfpCardEntity> implements WfpCardService {

    private static final Class<WfpCardDto> DTO_CLASS = WfpCardDto.class;
    private static final Class<WfpCardEntity> ENTITY_CLASS = WfpCardEntity.class;

    @Autowired
    public WfpCardServiceImpl(WfpCardRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    private final WfpCardRepository repository;

    @Transactional
    public void delete(UUID id) {
        checkPermission(id);
        super.delete(id);
    }

    @Override
    public List<UserCardDto> getMyCards() {
        SecurityUtil.checkUserByBanStatus();
        List<WfpCardEntity> entities = repository.getByOwnerId(SecurityUtil.getUserId());
        return converter.convert(entities, UserCardDto.class);
    }

    @Override
    public List<WfpCardDto> getCardsByOwnerId(UUID ownerId) {
        List<WfpCardEntity> entities = repository.getByOwnerId(SecurityUtil.getUserId());
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<UserCardDto> doFavorite(UUID idCard) {
        List<UserCardDto> result = null;
        checkPermission(idCard);
        List<WfpCardEntity> entities = repository.getByOwnerId(SecurityUtil.getUserId());
        if (CollectionUtils.isNotEmpty(entities)) {
            entities.forEach(f -> {
                if (f.getId().equals(idCard)) {
                    f.setFavorite(true);
                } else {
                    f.setFavorite(false);
                }
            });
            entities = repository.saveAll(entities);
            repository.flush();
            if (CollectionUtils.isNotEmpty(entities)) {
                result = converter.convert(entities, UserCardDto.class);
            }
        }
        return result;
    }

    private void checkPermission(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        SecurityUtil.checkUserByBanStatus();
        WfpCardEntity entity = repository.getById(id);
        if (entity == null) {
            throw new ClientException(CARD_NOT_FOUND);
        }
        if (!entity.getOwnerId().equals(SecurityUtil.getUserId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_CARD);
        }

    }
}
