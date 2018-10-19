package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhoneEntity, UUID> {

    void deleteUserPhoneEntityByUserId(UUID id);

    UserPhoneEntity getByUserId(UUID id);

    UserPhoneEntity getUserPhoneEntityByPhone(String phone);

    boolean existsUserPhoneEntityByPhone(String phone);

}
