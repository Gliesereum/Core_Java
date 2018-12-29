package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.BusinessEntity;
import com.gliesereum.share.common.model.dto.account.user.BusinessDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
public interface BusinessService extends DefaultService<BusinessDto, BusinessEntity> {

    void addUser(UUID idBusiness, UUID idUser);

    void removeUser(UUID idBusiness, UUID idUser);
}
