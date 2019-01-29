package com.gliesereum.karma.model.entity.record;

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
@Table(name = "order")
public class OrderEntity extends DefaultEntity {

    @Column(name = "service_id")
    private UUID serviceId;

    @Column(name = "record_id")
    private UUID recordId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "from_package")
    private Boolean fromPackage;
}
