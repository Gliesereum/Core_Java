package com.gliesereum.account.model.repository.jpa.user;

import com.gliesereum.account.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

}
