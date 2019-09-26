package com.gliesereum.payment.service.payment.impl;

import com.gliesereum.payment.model.entity.payment.PaymentRecipientEntity;
import com.gliesereum.payment.model.repository.jpa.payment.PaymentRecipientRepository;
import com.gliesereum.payment.service.payment.PaymentRecipientService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.payment.payment.PaymentRecipientDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.CryptoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.PaymentExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PaymentRecipientServiceImpl extends DefaultServiceImpl<PaymentRecipientDto, PaymentRecipientEntity> implements PaymentRecipientService {

    private static final Class<PaymentRecipientDto> DTO_CLASS = PaymentRecipientDto.class;
    private static final Class<PaymentRecipientEntity> ENTITY_CLASS = PaymentRecipientEntity.class;

    @Autowired
    public PaymentRecipientServiceImpl(PaymentRecipientRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    private final PaymentRecipientRepository repository;

    //@Value("${liq-pay.crypto.salt}")
    private String salt;

    //@Value("${liq-pay.crypto.password}")
    private String password;

    @Override
    @Transactional
    public PaymentRecipientDto create(PaymentRecipientDto dto) {
        checkModel(dto);
        dto.setPrivateKey(CryptoUtil.encryptAes256(dto.getPrivateKey(), password, salt));
        return super.create(dto);
    }

    @Override
    @Transactional
    public PaymentRecipientDto update(PaymentRecipientDto dto) {
        checkModel(dto);
        dto.setPrivateKey(CryptoUtil.encryptAes256(dto.getPrivateKey(), password, salt));
        return super.update(dto);
    }

    @Override
    public PaymentRecipientDto getById(UUID id) {
        PaymentRecipientDto result = null;
        if (id != null) {
            result = super.getById(id);
            if (result != null) {
                result.setPrivateKey(CryptoUtil.decryptAes256(result.getPrivateKey(), password, salt));
            }
        }
        return result;
    }

    public PaymentRecipientDto getByObjectIdAndType(UUID objectId) {
        PaymentRecipientDto result = null;
        if (ObjectUtils.allNotNull(objectId)) {
            PaymentRecipientEntity entity = repository.getByObjectId(objectId);
            if (entity != null) {
                result = converter.convert(entity, dtoClass);
            }
        }
        return result;
    }

    private void checkModel(PaymentRecipientDto dto) {
        if (dto == null)
            throw new ClientException(BODY_INVALID);
        if (dto.getObjectId() == null)
            throw new ClientException(OWNER_ID_EMPTY);
        if (StringUtils.isBlank(dto.getPrivateKey()))
            throw new ClientException(PRIVATE_KEY_EMPTY);
        if (StringUtils.isBlank(dto.getPublicKey()))
            throw new ClientException(PUBLIC_KEY_EMPTY);
    }

    @Override
    public PaymentRecipientDto getByPublicKey(String publicKey) {
        PaymentRecipientEntity entity = repository.getByPublicKey(publicKey);
        return converter.convert(entity, dtoClass);
    }
}
