package com.gliesereum.karma.model.entity.common;

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
@Table(name = "price_filter_attribute")
public class PriceFilterAttributeEntity extends DefaultEntity {

    @Column(name = "price_id")
    private UUID priceId;

    @Column(name = "filter_attribute_id")
    private UUID filterAttributeId;

}
