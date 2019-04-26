package com.gliesereum.share.common.model.dto.notification.device;

import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDeviceRegistrationDto extends UserDeviceDto {

    @Valid
    private List<UserSubscribeDto> subscribes;
}
