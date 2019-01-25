package com.gliesereum.karma.model.entity.carwash;

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
@Table(name = "car_wash_record_service")
public class CarWashRecordServiceEntity extends DefaultEntity {

    @Column(name = "car_wash_record_id")
    private UUID carWashRecordId;

    @Column(name = "service_id")
    private UUID serviceId;

}
