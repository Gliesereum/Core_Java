package com.gliesereum.notification.model.entity.device;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
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
@Table(name = "user_device")
@EntityListeners(AuditingEntityListener.class)
public class UserDeviceEntity extends DefaultEntity {

	@Column(name = "user_id")
	private UUID userId;

	@Column(name = "firebase_registration_token")
	private String firebaseRegistrationToken;

	@Column(name = "notification_enable")
	private Boolean notificationEnable;

	@Column(name = "create_date")
	@CreatedDate
	private LocalDateTime createDate;

	@Column(name = "update_date")
	@LastModifiedDate
	private LocalDateTime updateDate;

}