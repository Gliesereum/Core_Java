package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserEmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author vitalij
 */
@Repository
public interface UserEmailRepository extends JpaRepository<UserEmailEntity, UUID> {

    void deleteUserEmailEntityByUserId(UUID id);

    UserEmailEntity getUserEmailEntityByEmail(String email);

    boolean existsUserEmailEntityByEmail(String email);

    UserEmailEntity getByUserId(UUID id);
}
