package com.gliesereum.share.common.model.dto.notification.device;

import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDeviceRegistrationDto extends UserDeviceDto {

    private List<UserSubscribeDto> subscribes;
}
