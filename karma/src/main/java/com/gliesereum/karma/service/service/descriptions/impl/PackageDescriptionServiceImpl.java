package com.gliesereum.karma.service.service.descriptions.impl;

import com.gliesereum.karma.model.entity.service.descriptions.PackageDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.service.descriptions.PackageDescriptionRepository;
import com.gliesereum.karma.service.service.descriptions.PackageDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.PackageDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class PackageDescriptionServiceImpl extends DefaultDescriptionServiceImpl<PackageDescriptionDto, PackageDescriptionEntity> implements PackageDescriptionService {

    private static final Class<PackageDescriptionDto> DTO_CLASS = PackageDescriptionDto.class;
    private static final Class<PackageDescriptionEntity> ENTITY_CLASS = PackageDescriptionEntity.class;

    private final PackageDescriptionRepository packageDescriptionRepository;

    @Autowired
    public PackageDescriptionServiceImpl(PackageDescriptionRepository packageDescriptionRepository, DefaultConverter defaultConverter) {
        super(packageDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.packageDescriptionRepository = packageDescriptionRepository;
    }
}
