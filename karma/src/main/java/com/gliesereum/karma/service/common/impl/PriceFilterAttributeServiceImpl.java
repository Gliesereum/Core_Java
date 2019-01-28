package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.PriceFilterAttributeEntity;
import com.gliesereum.karma.model.repository.jpa.common.PriceFilterAttributeRepository;
import com.gliesereum.karma.service.common.PriceFilterAttributeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.PriceFilterAttributeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PriceFilterAttributeServiceImpl extends DefaultServiceImpl<PriceFilterAttributeDto, PriceFilterAttributeEntity> implements PriceFilterAttributeService {

    @Autowired
    private PriceFilterAttributeRepository repository;

    private static final Class<PriceFilterAttributeDto> DTO_CLASS = PriceFilterAttributeDto.class;
    private static final Class<PriceFilterAttributeEntity> ENTITY_CLASS = PriceFilterAttributeEntity.class;

    public PriceFilterAttributeServiceImpl(PriceFilterAttributeRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    @Transactional
    public void deleteByPriceIdAndFilterId(UUID idPrice, UUID filterAttributeId) {
        repository.deleteByPriceIdAndFilterAttributeId(idPrice, filterAttributeId);
    }

    @Override
    public boolean existByPriceIdAndAttributeId(UUID idPrice, UUID filterAttributeId) {
        return existByPriceIdAndAttributeId(idPrice, filterAttributeId);
    }

}
