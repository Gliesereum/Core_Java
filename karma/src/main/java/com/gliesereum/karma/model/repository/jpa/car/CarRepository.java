package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.CarEntity;
import com.gliesereum.share.common.repository.refreshable.RefreshableRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarRepository extends JpaRepository<CarEntity, UUID>, RefreshableRepository {

    List<CarEntity> getAllByUserId(UUID id);

    List<CarEntity> getAllByBrandIdIn(List<UUID> brandIds);

    void deleteAllByUserId(UUID id);

    boolean existsByIdAndUserId(UUID id, UUID userId);

    Optional<CarEntity> findByIdAndUserId(UUID carId, UUID userId);

    @Query("SELECT DISTINCT c.id FROM CarEntity c WHERE c.userId = :userId AND c.favorite = :favorite")
    List<UUID> getIdsByUserIdAndFavorite(UUID userId, Boolean favorite);
}
