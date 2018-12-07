package com.gliesereum.karma.model.entity.carwash;

import com.gliesereum.karma.model.entity.common.WorkTimeEntity;
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
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "car_wash")
public class CarWashEntity extends DefaultEntity {

    @Column(name = "user_business_id")
    private UUID userBusinessId;

    @Column(name = "name")
    private String name;

    @Column(name = "count_box")
    private int countBox;

    @Column(name = "description")
    private String description;

    @Column(name = "address")
    private String address;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "business_service_id")
    private Set<WorkTimeEntity> workTimes = new HashSet<>();
}
