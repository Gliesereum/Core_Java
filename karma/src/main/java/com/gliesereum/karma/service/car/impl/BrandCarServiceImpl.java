package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.BrandCarEntity;
import com.gliesereum.karma.model.repository.jpa.car.BrandCarRepository;
import com.gliesereum.karma.service.car.BrandCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.BrandCarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class BrandCarServiceImpl extends DefaultServiceImpl<BrandCarDto, BrandCarEntity> implements BrandCarService {

    private static final Class<BrandCarDto> DTO_CLASS = BrandCarDto.class;
    private static final Class<BrandCarEntity> ENTITY_CLASS = BrandCarEntity.class;

    public BrandCarServiceImpl(BrandCarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<BrandCarDto> getAll() {
        List<BrandCarDto> result = super.getAll();
        result.sort(Comparator.comparing(BrandCarDto::getName));
        return result;
    }
}
