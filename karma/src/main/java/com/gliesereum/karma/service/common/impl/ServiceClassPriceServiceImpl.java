package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.common.ServiceClassPriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServiceClassPriceRepository;
import com.gliesereum.karma.service.common.ServiceClassPriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassPriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class ServiceClassPriceServiceImpl extends DefaultServiceImpl<ServiceClassPriceDto, ServiceClassPriceEntity> implements ServiceClassPriceService {

    private static final Class<ServiceClassPriceDto> DTO_CLASS = ServiceClassPriceDto.class;
    private static final Class<ServiceClassPriceEntity> ENTITY_CLASS = ServiceClassPriceEntity.class;

    public ServiceClassPriceServiceImpl(ServiceClassPriceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    @UpdateCarWashIndex
    public ServiceClassPriceDto create(ServiceClassPriceDto dto) {
        return super.create(dto);
    }

    @Override
    @UpdateCarWashIndex
    public ServiceClassPriceDto update(ServiceClassPriceDto dto) {
        return super.update(dto);
    }
}
