package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.DividendEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.DividendRepository;
import com.gliesereum.lendinggallery.service.offer.DividendService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.DividendDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class DividendServiceImpl extends DefaultServiceImpl<DividendDto, DividendEntity> implements DividendService {

    private static final Class<DividendDto> DTO_CLASS = DividendDto.class;
    private static final Class<DividendEntity> ENTITY_CLASS = DividendEntity.class;

    private final DividendRepository dividendRepository;

    @Autowired
    public DividendServiceImpl(DividendRepository dividendRepository, DefaultConverter defaultConverter) {
        super(dividendRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.dividendRepository = dividendRepository;
    }
}
