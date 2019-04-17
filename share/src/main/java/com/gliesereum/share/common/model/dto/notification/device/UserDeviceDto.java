package com.gliesereum.share.common.model.dto.notification.device;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDeviceDto extends DefaultDto {

    private UUID userId;

    private String firebaseRegistrationToken;

    private Boolean notificationEnable;
}
