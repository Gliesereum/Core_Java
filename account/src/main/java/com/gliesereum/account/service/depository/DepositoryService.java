package com.gliesereum.account.service.depository;

import com.gliesereum.account.model.entity.DepositoryEntity;
import com.gliesereum.share.common.model.dto.account.user.DepositoryDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 2019-01-10
 */
public interface DepositoryService extends DefaultService<DepositoryDto, DepositoryEntity> {

    List<DepositoryDto> getByOwnerId(UUID ownerId);
}
