package com.gliesereum.karma.service.pincode;

import com.gliesereum.karma.model.entity.pincode.UserPinCodeEntity;
import com.gliesereum.share.common.model.dto.karma.pincode.UserPinCodeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserPinCodeService extends DefaultService<UserPinCodeDto, UserPinCodeEntity> {

    UserPinCodeDto getByUserId(UUID userId);

    UserPinCodeDto save(UserPinCodeDto userPinCode);

    void remindMe();

    void deleteByUserId(UUID userId);
}