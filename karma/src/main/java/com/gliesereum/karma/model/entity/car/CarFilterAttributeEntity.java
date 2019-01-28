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
@Table(name = "car_filter_attribute")
public class CarFilterAttributeEntity extends DefaultEntity {

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "filter_attribute_id")
    private UUID filterAttributeId;

}
