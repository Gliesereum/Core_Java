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
@Table(name = "city")
public class CityEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "centre_latitude")
    private Double centreLatitude;

    @Column(name = "centre_longitude")
    private Double centreLongitude;

    @Column(name = "country_id")
    private UUID countryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private CountryEntity country;

    @OneToMany
    @JoinColumn(name = "object_id", insertable = false, updatable = false)
    private Set<GeoPositionEntity> polygonPoints = new HashSet<>();
}