package com.gliesereum.karma.model.entity.car;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "car_service_class_car")
public class CarServiceClassCarEntity extends DefaultEntity {

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "service_class_car_id")
    private UUID serviceClassId;

}
