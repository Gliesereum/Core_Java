package com.gliesereum.payment.service.payment;

import com.gliesereum.payment.model.entity.payment.PaymentRecipientEntity;
import com.gliesereum.share.common.model.dto.payment.PaymentRecipientDto;
import com.gliesereum.share.common.service.DefaultService;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PaymentRecipientService extends DefaultService<PaymentRecipientDto, PaymentRecipientEntity> {

    PaymentRecipientDto getByPublicKey(String publicKey);
}
