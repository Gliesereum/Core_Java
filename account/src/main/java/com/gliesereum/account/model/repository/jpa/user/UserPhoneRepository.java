package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */

public interface UserPhoneRepository extends JpaRepository<UserPhoneEntity, UUID> {

    void deleteUserPhoneEntityByUserId(UUID id);

    UserPhoneEntity getByUserId(UUID id);

    List<UserPhoneEntity> getByUserIdIn(List<UUID> ids);

    UserPhoneEntity getUserPhoneEntityByPhone(String phone);

    boolean existsUserPhoneEntityByPhone(String phone);

}
