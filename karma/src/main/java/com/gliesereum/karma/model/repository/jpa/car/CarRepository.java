package com.gliesereum.karma.model.repository.jpa.car;

import com.gliesereum.karma.model.entity.car.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Repository
public interface CarRepository extends JpaRepository<CarEntity, UUID> {

    List<CarEntity> getAllByUserId(UUID id);

    void deleteAllByUserId(UUID id);

    boolean existsCarEntityByUserIdAndId(UUID idUser,UUID idCar);
}
