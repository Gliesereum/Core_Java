package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
