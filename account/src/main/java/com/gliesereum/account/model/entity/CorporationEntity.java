package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vitalij
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "corporation")
public class CorporationEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "edrpou")
    private String edrpou;

    @Column(name = "address")
    private String address;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "kyc_status")
    @Enumerated(EnumType.STRING)
    private KYCStatus kYCStatus;

    @Column(name = "verified_status")
    @Enumerated(EnumType.STRING)
    private VerifiedStatus verifiedStatus;

    @OneToMany
    @JoinColumn(name = "corporation_id", insertable = false, updatable = false)
    private Set<CorporationSharedOwnershipEntity> corporationSharedOwnerships = new HashSet<>();
}
