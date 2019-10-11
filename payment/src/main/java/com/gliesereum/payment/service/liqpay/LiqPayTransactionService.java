package com.gliesereum.payment.service.liqpay;

import com.gliesereum.payment.model.liqpay.LiqPayTransactionEntity;
import com.gliesereum.share.common.model.dto.payment.liqpay.LiqPayTransactionDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

public interface LiqPayTransactionService extends DefaultService<LiqPayTransactionDto, LiqPayTransactionEntity> {

    LiqPayTransactionDto getByOrderId(UUID orderId);
}