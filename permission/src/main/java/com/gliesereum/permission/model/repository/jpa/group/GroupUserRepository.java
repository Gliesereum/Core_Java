package com.gliesereum.permission.model.repository.jpa.group;

import com.gliesereum.permission.model.entity.group.GroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity, UUID> {

    GroupUserEntity findByUserId(UUID userId);
}
