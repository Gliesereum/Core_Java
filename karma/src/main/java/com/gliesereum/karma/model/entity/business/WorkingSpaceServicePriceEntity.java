package com.gliesereum.karma.model.entity.business;

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
@Table(name = "working_space_price")
public class WorkingSpaceServicePriceEntity extends DefaultEntity {

    @Column(name = "price_id")
    private UUID priceId;

    @Column(name = "working_space_id")
    private UUID workingSpaceId;
}