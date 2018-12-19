package com.gliesereum.karma.model.entity.common;

import com.gliesereum.share.common.model.entity.DefaultEntity;
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
 * @since 2018-12-07
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "package")
public class PackageEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "discount")
    private int discount;

    @Column(name = "duration")
    private int duration;

    @Column(name = "business_service_id")
    private UUID businessServiceId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "package_service",
            joinColumns = {@JoinColumn(name = "package_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "service_id", insertable = false, updatable = false)})
    private Set<ServicePriceEntity> services = new HashSet<>();
}
