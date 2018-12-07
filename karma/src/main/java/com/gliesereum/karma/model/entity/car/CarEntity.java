package com.gliesereum.karma.model.entity.car;

import com.gliesereum.share.common.model.dto.karma.enumerated.CarInteriorType;
import com.gliesereum.share.common.model.dto.karma.enumerated.CarType;
import com.gliesereum.share.common.model.dto.karma.enumerated.ColourCarType;
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
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "car")
public class CarEntity extends DefaultEntity {

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(name = "model_id")
    private UUID modelId;

    @Column(name = "year_id")
    private UUID yearId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", insertable = false, updatable = false)
    private BrandCarEntity brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", insertable = false, updatable = false)
    private ModelCarEntity model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id", insertable = false, updatable = false)
    private YearCarEntity year;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "description")
    private String description;

    @Column(name = "note")
    private String note;

    @Column(name = "interior")
    @Enumerated(EnumType.STRING)
    private CarInteriorType interior;

    @Column(name = "car_body")
    @Enumerated(EnumType.STRING)
    private CarType carBody;

    @Column(name = "colour")
    @Enumerated(EnumType.STRING)
    private ColourCarType colour;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "car_service_class_car",
            joinColumns = {@JoinColumn(name = "car_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "service_class_car_id", insertable = false, updatable = false)})
    private Set<ServiceClassCarEntity> services = new HashSet<>();

}
