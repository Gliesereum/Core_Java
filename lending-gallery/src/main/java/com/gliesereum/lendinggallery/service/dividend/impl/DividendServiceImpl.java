package com.gliesereum.lendinggallery.service.dividend.impl;

import com.gliesereum.lendinggallery.model.entity.dividend.DividendEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.dividend.DividendRepository;
import com.gliesereum.lendinggallery.service.dividend.DividendService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.dividend.DividendDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class DividendServiceImpl extends DefaultServiceImpl<DividendDto, DividendEntity> implements DividendService {

    private final DividendRepository repository;

    private static final Class<DividendDto> DTO_CLASS = DividendDto.class;
    private static final Class<DividendEntity> ENTITY_CLASS = DividendEntity.class;

    public DividendServiceImpl(DividendRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }
}
