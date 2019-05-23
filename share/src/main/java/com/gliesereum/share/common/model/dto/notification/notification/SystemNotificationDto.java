package com.gliesereum.share.common.model.dto.notification.notification;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class SystemNotificationDto<T> {

    private UUID objectId;

    private T object;

}
