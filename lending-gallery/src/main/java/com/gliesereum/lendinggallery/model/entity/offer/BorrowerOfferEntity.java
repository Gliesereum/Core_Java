package com.gliesereum.lendinggallery.model.entity.offer;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "borrower_offer")
public class BorrowerOfferEntity extends DefaultEntity {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "description")
    private String description;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "state_type")
    @Enumerated(EnumType.STRING)
    private OfferStateType stateType;
}