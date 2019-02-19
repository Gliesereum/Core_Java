package com.gliesereum.account.model.entity;

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
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "corporation_shared_ownership")
public class CorporationSharedOwnershipEntity extends DefaultEntity {

    @Column(name = "share")
    private Integer share;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "corporation_id")
    private UUID corporationId;

    @Column(name = "owner_corporation_id")
    private UUID corporationOwnerId;
}
