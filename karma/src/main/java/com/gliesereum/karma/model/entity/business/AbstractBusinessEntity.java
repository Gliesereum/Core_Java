package com.gliesereum.karma.model.entity.business;

import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
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
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class AbstractBusinessEntity extends AuditableDefaultEntity {

    @Column(name = "corporation_id")
    private UUID corporationId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "site_url")
    private String siteUrl;
    
    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "cashless_payment")
    private boolean cashlessPayment;

    @Column(name = "business_verify")
    private Boolean businessVerify;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "add_phone")
    private String addPhone;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "time_zone")
    private Integer timeZone;

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_category_id", insertable = false, updatable = false)
    private BusinessCategoryEntity businessCategory;
}
