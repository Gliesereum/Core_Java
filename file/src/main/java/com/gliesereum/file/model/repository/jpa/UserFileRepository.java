package com.gliesereum.file.model.repository.jpa;

import com.gliesereum.file.model.entity.UserFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserFileRepository extends JpaRepository<UserFileEntity, UUID> {

    List<UserFileEntity> findAllByUserId(UUID userId);

    /**
     * Files owned by the user, or shared with them. `readerIds` is an
     * @ElementCollection, so CONTAINING becomes a MEMBER OF subquery and takes
     * the single reader id to look for, not a collection.
     */
    List<UserFileEntity> findAllByUserIdOrReaderIdsContains(UUID userId, UUID readerId);
}
