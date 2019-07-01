package com.gliesereum.karma.service.business;

import com.gliesereum.karma.model.entity.business.BaseBusinessEntity;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BusinessFullModel;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseBusinessService extends DefaultService<BaseBusinessDto, BaseBusinessEntity> {

    List<BaseBusinessDto> getAllIgnoreState();

    List<LiteBusinessDto> getAllLite();

    boolean existByIdAndCorporationIds(UUID id, List<UUID> corporationIds);

    boolean currentUserHavePermissionToActionInBusinessLikeOwner(UUID businessId);

    boolean currentUserHavePermissionToActionInBusinessLikeWorker(UUID businessId);

    boolean currentUserHavePermissionToActionInCorporationLikeWorker(UUID corporationId);

    List<BaseBusinessDto> getByCorporationIds(List<UUID> corporationIds);

    List<BusinessFullModel> getFullModelByIds(List<UUID> id);

    List<BaseBusinessDto> getByCorporationId(UUID corporationId);

    BaseBusinessDto getByIdIgnoreState(UUID id);

    List<BaseBusinessDto> getAllBusinessByCurrentUser();

    List<BusinessFullModel> getAllFullBusinessByCurrentUser();

    void deleteByCorporationIdCheckPermission(UUID id);

    void deleteByCorporationId(UUID id);

    List<UUID> searchClient(RecordsSearchDto search);

    List<PublicUserDto> getCustomersByBusinessIds(List<UUID> ids);

    List<UUID> getIdsByCorporationIds(List<UUID> corporationIds);
}

