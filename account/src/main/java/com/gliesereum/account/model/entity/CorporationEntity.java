package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @Column(name = "company_type")
    private String companyType;

    @Column(name = "index")
    private String index;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "street")
    private String street;

    @Column(name = "building_number")
    private String buildingNumber;

    @Column(name = "office_number")
    private String officeNumber;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "business_activity")
    private String businessActivity;

    @Column(name = "rc_number")
    private String rcNumber;

    @Column(name = "place_incorporation")
    private String placeIncorporation;

    @Column(name = "date_incorporation")
    private LocalDateTime dateIncorporation;

    @Column(name = "kyc_status")
    @Enumerated(EnumType.STRING)
    private KYCStatus kYCStatus;

    @Column(name = "verified_status")
    @Enumerated(EnumType.STRING)
    private VerifiedStatus verifiedStatus;

    @OneToMany
    @JoinColumn(name = "corporation_id", insertable = false, updatable = false)
    private Set<CorporationSharedOwnershipEntity> corporationSharedOwnerships = new HashSet<>();

    @OneToMany
    @JoinColumn(name = "corporation_id", insertable = false, updatable = false)
    private Set<CorporationEmployeeEntity> corporationEmployees = new HashSet<>();
}
