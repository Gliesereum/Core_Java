package com.gliesereum.share.common.model.dto.notification.subscribe;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
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
public class UserSubscribeDto extends DefaultDto {

    private UUID userDeviceId;

    private SubscribeDestination subscribeDestination;

    private UUID objectId;

    private Boolean notificationEnable;
}
