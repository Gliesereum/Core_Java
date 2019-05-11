package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.model.entity.service.PackageServiceEntity;
import com.gliesereum.karma.model.repository.jpa.service.PackageServiceRepository;
import com.gliesereum.karma.service.service.PackageServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.service.PackageServiceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PackageServiceServiceImpl extends DefaultServiceImpl<PackageServiceDto, PackageServiceEntity> implements PackageServiceService {

    private static final Class<PackageServiceDto> DTO_CLASS = PackageServiceDto.class;
    private static final Class<PackageServiceEntity> ENTITY_CLASS = PackageServiceEntity.class;

    private final PackageServiceRepository packageServiceRepository;

    @Autowired
    public PackageServiceServiceImpl(PackageServiceRepository packageServiceRepository, DefaultConverter defaultConverter) {
        super(packageServiceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.packageServiceRepository = packageServiceRepository;
    }

    @Override
    @Transactional
    public void deleteByPackageIdAndServicePriceIDs(UUID packageId, List<UUID> servicePricesIds) {
        packageServiceRepository.deleteAllByPackageIdAndServiceIdIn(packageId, servicePricesIds);
    }

    @Override
    @Transactional
    public void deleteByPackageId(UUID packageId) {
        packageServiceRepository.deleteAllByPackageId(packageId);
    }

    @Override
    public PackageServiceDto create(PackageServiceDto dto) {
        if (dto != null)
            checkPackageServiceExist(dto.getPackageId(), dto.getServiceId());
        return super.create(dto);
    }

    @Override
    public PackageServiceDto update(PackageServiceDto dto) {
        if (dto != null)
            checkPackageServiceExist(dto.getPackageId(), dto.getServiceId());
        return super.update(dto);
    }

    private void checkPackageServiceExist(UUID packageId, UUID serviceId) {
        if (packageId == null){
            throw new ClientException(PACKAGE_ID_IS_EMPTY);
        }
        if (serviceId == null){
            throw new ClientException(SERVICE_CLASS_ID_IS_EMPTY);
        }
        if(packageServiceRepository.existsByPackageIdAndServiceId(packageId, serviceId)){
            throw new ClientException(PAR_SERVICE_CLASS_ID_AND_PACKAGE_ID_EXIST);
        }
    }
}
