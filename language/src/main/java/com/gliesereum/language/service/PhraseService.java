package com.gliesereum.language.service;

import com.gliesereum.language.model.entity.PhraseEntity;
import com.gliesereum.share.common.model.dto.language.PhraseDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface PhraseService extends DefaultService<PhraseDto, PhraseEntity> {

    void deleteByPackageId(UUID packageId);
}