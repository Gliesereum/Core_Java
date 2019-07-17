package com.gliesereum.permission.service.application;

import com.gliesereum.permission.model.entity.application.ApplicationEntity;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface ApplicationService extends DefaultService<ApplicationDto, ApplicationEntity> {

    ApplicationDto check(UUID id);

}