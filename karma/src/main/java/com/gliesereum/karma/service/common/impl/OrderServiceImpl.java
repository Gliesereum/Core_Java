package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.OrderEntity;
import com.gliesereum.karma.model.repository.jpa.common.OrderRepository;
import com.gliesereum.karma.service.common.OrderService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.OrderDto;
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
