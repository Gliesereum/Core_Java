package com.gliesereum.notification.model.repository.jpa.device;

import com.gliesereum.notification.model.entity.device.UserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserDeviceRepository extends JpaRepository<UserDeviceEntity, UUID> {

}