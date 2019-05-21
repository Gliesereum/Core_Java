package com.gliesereum.share.common.model.dto.notification.notification;

import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class SendNotificationDto {

    @NotNull
    private SubscribeDestination destination;

    @NotEmpty
    private String title;

    @NotEmpty
    private String body;

    @NotNull
    private List<UUID> userIds;

    @NotNull
    private UUID businessId;
}
