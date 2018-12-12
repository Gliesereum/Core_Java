package com.gliesereum.karma.service.karma;

import com.gliesereum.karma.model.entity.karma.KarmaEntity;
import com.gliesereum.share.common.model.dto.karma.karma.KarmaDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */
public interface KarmaService extends DefaultService<KarmaDto, KarmaEntity> {

    KarmaDto getByUserId(UUID userId);
}
