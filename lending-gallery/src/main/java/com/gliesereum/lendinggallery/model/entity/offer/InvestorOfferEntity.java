package com.gliesereum.lendinggallery.model.entity.offer;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OfferStateType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "investor_offer")
public class InvestorOfferEntity extends DefaultEntity {

    @Column(name = "sum_investment")
    private Integer sumInvestment;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "art_bond_id")
    private UUID artBondId;

    @Column(name = "create_date")
    private LocalDateTime create;

    @Column(name = "state_type")
    @Enumerated(EnumType.STRING)
    private OfferStateType stateType;

    @OneToMany
    @OrderBy("create DESC")
    @JoinColumn(name = "offer_id", insertable = false, updatable = false)
    private Set<OfferCommentEntity> comments = new HashSet<>();

}