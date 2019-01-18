package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.PackageServiceEntity;
import com.gliesereum.share.common.model.dto.karma.common.PackageServiceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface PackageServiceService extends DefaultService<PackageServiceDto, PackageServiceEntity> {

    void deleteByPackageIdAndServicePriceIDs(UUID packageId, List<UUID> servicePricesIds);
}
