package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseBusinessService extends DefaultService<BaseBusinessDto, BaseBusinessEntity> {

    boolean existByIdAndCorporationIds(UUID id, List<UUID> corporationIds);

    boolean currentUserHavePermissionToActionInBusiness(UUID businessId);

    List<BaseBusinessDto> getByCorporationIds(List<UUID> corporationIds);

    BusinessFullModel getFullModelById(UUID id);

    List<BaseBusinessDto> getByCorporationId(UUID corporationId);
}