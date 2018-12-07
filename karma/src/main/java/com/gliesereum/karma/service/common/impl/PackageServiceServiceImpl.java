package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.PackageServiceEntity;
import com.gliesereum.karma.model.repository.jpa.common.PackageServiceRepository;
import com.gliesereum.karma.service.common.PackageServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.PackageServiceDto;
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
public class PackageServiceServiceImpl extends DefaultServiceImpl<PackageServiceDto, PackageServiceEntity> implements PackageServiceService {

    private static final Class<PackageServiceDto> DTO_CLASS = PackageServiceDto.class;
    private static final Class<PackageServiceEntity> ENTITY_CLASS = PackageServiceEntity.class;

    public PackageServiceServiceImpl(PackageServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
