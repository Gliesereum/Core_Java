package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
public interface CorporationService extends DefaultService<CorporationDto, CorporationEntity> {

    void addUser(UUID idCorporation, UUID idUser);

    void removeUser(UUID idCorporation, UUID idUser);
}
