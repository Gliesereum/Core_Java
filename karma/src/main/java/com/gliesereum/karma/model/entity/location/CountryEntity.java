package com.gliesereum.karma.model.entity.location;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "country")
public class CountryEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "centre_latitude")
    private Double centreLatitude;

    @Column(name = "centre_longitude")
    private Double centreLongitude;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<GeoPositionEntity> polygonPoints = new HashSet<>();
}