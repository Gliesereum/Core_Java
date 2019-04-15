package com.gliesereum.lendinggallery.model.entity.artbond;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "interested_art_bond")
public class InterestedArtBondEntity extends DefaultEntity {

    @Column(name = "art_bond_id")
    private UUID artBondId;

    @Column(name = "customer_id")
    private UUID customerId;
}