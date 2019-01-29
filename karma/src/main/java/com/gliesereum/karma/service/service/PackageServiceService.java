package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.PackageServiceEntity;
import com.gliesereum.share.common.model.dto.karma.service.PackageServiceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface PackageServiceService extends DefaultService<PackageServiceDto, PackageServiceEntity> {

    void deleteByPackageIdAndServicePriceIDs(UUID packageId, List<UUID> servicePricesIds);
}
