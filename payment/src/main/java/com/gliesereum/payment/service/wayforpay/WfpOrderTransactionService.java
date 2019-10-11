package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpOrderTransactionEntity;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpOrderTransactionDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

public interface WfpOrderTransactionService extends DefaultService<WfpOrderTransactionDto, WfpOrderTransactionEntity> {

    List<WfpOrderTransactionDto> getByOrderId(UUID orderId);
}