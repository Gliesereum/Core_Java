package com.gliesereum.notification.model.repository.jpa.device;

import com.gliesereum.notification.model.entity.device.UserDeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserDeviceRepository extends JpaRepository<UserDeviceEntity, UUID> {

    boolean existsByFirebaseRegistrationToken(String registrationToken);

    UserDeviceEntity findByFirebaseRegistrationToken(String registrationToken);

    List<UserDeviceEntity> findAllByUserId(UUID userId);

}