package com.gliesereum.notification.model.repository.jpa.subscribe;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserSubscribeRepository
		extends JpaRepository<UserSubscribeEntity, UUID> {

}