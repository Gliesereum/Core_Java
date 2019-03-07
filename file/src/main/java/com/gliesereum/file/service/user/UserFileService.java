package com.gliesereum.file.service.user;

import com.gliesereum.file.model.entity.UserFileEntity;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserFileService extends DefaultService<UserFileDto, UserFileEntity> {

    List<UserFileDto> getByUserId(UUID userId);

    List<UserFileDto> getAllCanRead(UUID userId);

    UserFileDto updateCurrentUser(UserFileDto userFileDto);

    void deleteCurrentUser(UUID fileId);
}
