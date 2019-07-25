package com.gliesereum.karma.service.administrator;

import com.gliesereum.karma.model.entity.administator.BusinessAdministratorEntity;
import com.gliesereum.share.common.model.dto.karma.administrator.BusinessAdministratorDto;
import com.gliesereum.share.common.model.dto.karma.administrator.DetailedBusinessAdministratorDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface BusinessAdministratorService extends DefaultService<BusinessAdministratorDto, BusinessAdministratorEntity> {

    DetailedBusinessAdministratorDto create(UUID userId, UUID businessId);

    void delete(UUID userId, UUID businessId);

    boolean existByUserIdBusinessId(UUID businessId, UUID userId);

    boolean existByUserIdCorporationId(UUID corporationId, UUID userId);

    List<DetailedBusinessAdministratorDto> getByBusinessId(UUID businessId);

    List<DetailedBusinessAdministratorDto> getByCorporationId(UUID corporationId);
}