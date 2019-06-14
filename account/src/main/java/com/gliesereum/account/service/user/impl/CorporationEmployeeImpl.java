package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationEmployeeEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationEmployeeRepository;
import com.gliesereum.account.service.user.CorporationEmployeeService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.user.CorporationEmployeeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class CorporationEmployeeImpl extends DefaultServiceImpl<CorporationEmployeeDto, CorporationEmployeeEntity> implements CorporationEmployeeService {

    private static final Class<CorporationEmployeeDto> DTO_CLASS = CorporationEmployeeDto.class;
    private static final Class<CorporationEmployeeEntity> ENTITY_CLASS = CorporationEmployeeEntity.class;

    private final CorporationEmployeeRepository corporationEmployeeRepository;

    @Autowired
    private CorporationService corporationService;

    @Autowired
    public CorporationEmployeeImpl(CorporationEmployeeRepository corporationEmployeeRepository, DefaultConverter defaultConverter) {
        super(corporationEmployeeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.corporationEmployeeRepository = corporationEmployeeRepository;
    }

    @Override
    public List<CorporationEmployeeDto> getAllByCorporationId(UUID id) {
        List<CorporationEmployeeEntity> entities = corporationEmployeeRepository.findByCorporationId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public CorporationEmployeeDto create(CorporationEmployeeDto dto) {
        checkModel(dto);
        return super.create(dto);
    }

    @Override
    @Transactional
    public CorporationEmployeeDto update(CorporationEmployeeDto dto) {
        checkModel(dto);
        return super.update(dto);
    }

    private void checkModel(CorporationEmployeeDto dto) {
        corporationService.checkCurrentUserForPermissionActionThisCorporation(dto.getCorporationId());
    }
}
