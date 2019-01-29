package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.model.entity.record.OrderEntity;
import com.gliesereum.karma.model.repository.jpa.record.OrderRepository;
import com.gliesereum.karma.service.record.OrderService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.record.OrderDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class OrderServiceImpl extends DefaultServiceImpl<OrderDto, OrderEntity> implements OrderService {

    private static final Class<OrderDto> DTO_CLASS = OrderDto.class;
    private static final Class<OrderEntity> ENTITY_CLASS = OrderEntity.class;

    public OrderServiceImpl(OrderRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }
}
