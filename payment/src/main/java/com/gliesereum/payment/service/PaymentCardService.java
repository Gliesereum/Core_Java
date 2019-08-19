package com.gliesereum.payment.service;

import com.gliesereum.payment.model.entity.PaymentCardEntity;
import com.gliesereum.share.common.model.dto.payment.PaymentCardDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentCardService extends DefaultService<PaymentCardDto, PaymentCardEntity> {

    List<PaymentCardDto> getMyCards();

    List<PaymentCardDto> makeFavorite(UUID idCard);
}
