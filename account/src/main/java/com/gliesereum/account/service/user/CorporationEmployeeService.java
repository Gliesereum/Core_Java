package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationEmployeeEntity;
import com.gliesereum.share.common.model.dto.account.user.CorporationEmployeeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface CorporationEmployeeService extends DefaultService<CorporationEmployeeDto, CorporationEmployeeEntity> {

    List<CorporationEmployeeDto> getAllByCorporationId(UUID id);
}
