package com.gliesereum.payment.service.wayforpay.impl;

import com.gliesereum.payment.model.entity.wayforpay.WfpOrderEntity;
import com.gliesereum.payment.model.repository.jpa.wayforpay.WfpOrderRepository;
import com.gliesereum.payment.service.wayforpay.WfpOrderService;
import com.gliesereum.payment.service.wayforpay.WfpOrderTransactionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpOrderDto;
import com.gliesereum.share.common.model.dto.payment.wayforpay.WfpOrderTransactionDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WfpOrderServiceImpl extends DefaultServiceImpl<WfpOrderDto, WfpOrderEntity> implements WfpOrderService {

    private static final Class<WfpOrderDto> DTO_CLASS = WfpOrderDto.class;
    private static final Class<WfpOrderEntity> ENTITY_CLASS = WfpOrderEntity.class;

    @Autowired
    public WfpOrderServiceImpl(WfpOrderRepository wfpOrderRepository, DefaultConverter defaultConverter) {
        super(wfpOrderRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.wfpOrderRepository = wfpOrderRepository;
    }

    private final WfpOrderRepository wfpOrderRepository;

    @Autowired
    private WfpOrderTransactionService transactionService;

    @Override
    public WfpOrderDto getByOrderId(UUID orderId) {
        WfpOrderDto result = null;
        WfpOrderEntity entity = wfpOrderRepository.getByOrderReference(orderId);
        result = converter.convert(entity, dtoClass);
        if (result != null) {
            List<WfpOrderTransactionDto> transactions = transactionService.getByOrderId(result.getId());
            result.setTransactions(transactions);
        }
        return result;
    }

}
