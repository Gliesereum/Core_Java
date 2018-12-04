package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_business")
public class UserBusinessEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "user_id")
    private UUID userId;

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
}
