package com.gliesereum.karma.service.preference;

import com.gliesereum.karma.model.entity.preference.ClientPreferenceEntity;
import com.gliesereum.share.common.model.dto.karma.preference.ClientPreferenceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ClientPreferenceService extends DefaultService<ClientPreferenceDto, ClientPreferenceEntity> {

    List<ClientPreferenceDto> addListByServiceIds(List<UUID> serviceIds);

    ClientPreferenceDto addPreferenceByServiceId(UUID id);

    List<ClientPreferenceDto> getAllByUser();

    List<ClientPreferenceDto> getAllByUserId(UUID id);

    List<ClientPreferenceDto> getAllByUserIdAndBusinessCategoryIds(UUID userId, List<UUID> businessCategoryId);

    void deleteByServiceId(UUID id);

    void deleteAllByUser();
}