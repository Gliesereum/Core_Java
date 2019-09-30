package com.gliesereum.payment.service.wayforpay;

import com.gliesereum.payment.model.entity.wayforpay.WfpOrderEntity;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpOrderDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface WfpOrderService extends DefaultService<WfpOrderDto, WfpOrderEntity> {

    WfpOrderDto getByOrderId(UUID orderId);
}    