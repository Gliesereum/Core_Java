package com.gliesereum.payment.service.impl;

import com.gliesereum.payment.model.entity.PaymentOrderEntity;
import com.gliesereum.payment.model.repository.jpa.PaymentOrderRepository;
import com.gliesereum.payment.service.PaymentOrderService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.payment.PaymentOrderDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PaymentOrderServiceImpl extends DefaultServiceImpl<PaymentOrderDto, PaymentOrderEntity> implements PaymentOrderService {

    private static final Class<PaymentOrderDto> DTO_CLASS = PaymentOrderDto.class;
    private static final Class<PaymentOrderEntity> ENTITY_CLASS = PaymentOrderEntity.class;

    @Autowired
    public PaymentOrderServiceImpl(PaymentOrderRepository paymentOrderRepository, DefaultConverter defaultConverter) {
        super(paymentOrderRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.paymentOrderRepository = paymentOrderRepository;
    }

    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    @Transactional
    public PaymentOrderDto create(PaymentOrderDto dto) {
        return super.create(dto);
    }

    @Override
    @Transactional
    public PaymentOrderDto update(PaymentOrderDto dto) {
        return super.update(dto);
    }

    @Override
    public PaymentOrderDto getById(UUID id) {
        return super.getById(id);
    }

    @Transactional
    public void delete(UUID id) {
        super.delete(id);
    }
}
