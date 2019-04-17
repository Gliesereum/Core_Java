package com.gliesereum.notification.model.entity.subscribe;

import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_subscribe")
public class UserSubscribeEntity extends DefaultEntity {

	@Column(name = "user_device_id")
	private UUID userDeviceId;

	@Column(name = "subscribe_destination")
	@Enumerated(EnumType.STRING)
	private SubscribeDestination subscribeDestination;

	@Column(name = "object_id")
	private UUID objectId;

	@Column(name = "notification_enable")
	private Boolean notificationEnable;

}