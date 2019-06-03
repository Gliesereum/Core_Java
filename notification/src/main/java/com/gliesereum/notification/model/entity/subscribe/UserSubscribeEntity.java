package com.gliesereum.notification.model.entity.subscribe;

import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@EntityListeners(AuditingEntityListener.class)
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

	@Column(name = "create_date")
	@CreatedDate
	private LocalDateTime createDate;

	@Column(name = "update_date")
	@LastModifiedDate
	private LocalDateTime updateDate;

}