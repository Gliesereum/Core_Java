package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.YearCarEntity;
import com.gliesereum.karma.model.repository.jpa.car.YearCarRepository;
import com.gliesereum.karma.service.car.YearCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.YearCarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class YearCarServiceImpl extends DefaultServiceImpl<YearCarDto, YearCarEntity> implements YearCarService {

    private static final Class<YearCarDto> DTO_CLASS = YearCarDto.class;
    private static final Class<YearCarEntity> ENTITY_CLASS = YearCarEntity.class;

    public YearCarServiceImpl(YearCarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }
}
