package com.gliesereum.karma.model.entity.business;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public class AbstractBusinessEntity extends DefaultEntity {

    @Column(name = "corporation_id")
    private UUID corporationId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

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

    //TODO: remove
//    @Column(name = "service_type")
//    @Enumerated(EnumType.STRING)
//    private ServiceType serviceType;

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @Column(name = "object_state")
    @Enumerated(EnumType.STRING)
    private ObjectState objectState;
}
