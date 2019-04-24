package com.gliesereum.karma.model.entity.service;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
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
@Table(name = "service")
public class ServiceEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

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
