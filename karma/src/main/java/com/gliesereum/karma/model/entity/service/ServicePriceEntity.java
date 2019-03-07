package com.gliesereum.karma.model.entity.service;

import com.gliesereum.karma.model.entity.filter.FilterAttributeEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
@Table(name = "service_price")
public class ServicePriceEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "service_id")
    private UUID serviceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private ServiceEntity service;

    @Column(name = "business_id")
    private UUID businessId;

    @Column(name ="duration")
    private Integer duration;

    @Column(name = "object_state")
    @Enumerated(EnumType.STRING)
    private ObjectState objectState;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "service_class_price",
            joinColumns = {@JoinColumn(name = "price_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "service_class_id", insertable = false, updatable = false)})
    private Set<ServiceClassEntity> serviceClass = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "price_filter_attribute",
            joinColumns = {@JoinColumn(name = "price_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "filter_attribute_id", insertable = false, updatable = false)})
    private Set<FilterAttributeEntity> attributes = new HashSet<>();

}
