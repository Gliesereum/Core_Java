package com.gliesereum.karma.model.entity.location;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "district")
public class DistrictEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "city_id")
    private UUID cityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private CityEntity city;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<GeoPositionEntity> polygonPoints = new HashSet<>();
}