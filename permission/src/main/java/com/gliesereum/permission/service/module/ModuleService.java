package com.gliesereum.permission.service.module;

import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.service.DefaultService;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ModuleService extends DefaultService<ModuleDto, ModuleEntity> {

    ModuleDto getByUrl(String url);

    ModuleDto getByUrlOrCreate(String url, String name);
}
