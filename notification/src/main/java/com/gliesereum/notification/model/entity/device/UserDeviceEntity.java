package com.gliesereum.notification.model.entity.device;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_device")
public class UserDeviceEntity extends DefaultEntity {

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "firebase_registration_token")
	private String firebaseRegistrationToken;

	@Column(name = "notification_enable")
	private Boolean notificationEnable;

}