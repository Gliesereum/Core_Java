package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "business")
public class BusinessEntity extends DefaultEntity {

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
}
