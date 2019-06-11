package com.gliesereum.permission.model.repository.jpa.group;

import com.gliesereum.permission.model.entity.group.GroupUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Repository
public interface GroupUserRepository extends JpaRepository<GroupUserEntity, UUID> {

    List<GroupUserEntity> findByUserId(UUID userId);

    GroupUserEntity findByGroupIdAndUserId(UUID groupId, UUID userId);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupUserEntity> findAllByGroupIdIn(List<UUID> groupIds);
}
