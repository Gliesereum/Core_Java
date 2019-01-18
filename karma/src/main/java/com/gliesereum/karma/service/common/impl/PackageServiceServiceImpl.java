package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.PackageServiceEntity;
import com.gliesereum.karma.model.repository.jpa.common.PackageServiceRepository;
import com.gliesereum.karma.service.common.PackageServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.PackageServiceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class PackageServiceServiceImpl extends DefaultServiceImpl<PackageServiceDto, PackageServiceEntity> implements PackageServiceService {

    @Autowired
    private PackageServiceRepository repository;

    private static final Class<PackageServiceDto> DTO_CLASS = PackageServiceDto.class;
    private static final Class<PackageServiceEntity> ENTITY_CLASS = PackageServiceEntity.class;

    public PackageServiceServiceImpl(PackageServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public void deleteByPackageIdAndServicePriceIDs(UUID packageId, List<UUID> servicePricesIds) {
        repository.deleteAllByPackageIdAndServiceIdIn(packageId, servicePricesIds);
    }
}
