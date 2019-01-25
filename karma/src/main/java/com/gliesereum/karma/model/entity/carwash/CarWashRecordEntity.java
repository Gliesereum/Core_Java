package com.gliesereum.karma.model.entity.carwash;

import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusWashing;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
@Table(name = "car_wash_record")
public class CarWashRecordEntity extends DefaultEntity {

    @Column(name = "car_id")
    private UUID carId;

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "working_space_id")
    private UUID workingSpaceId;

    @Column(name = "car_wash_id")
    private UUID carWashId;

    @Column(name = "price")
    private Integer price;

    @Column(name = "begin")
    private LocalDateTime begin;

    @Column(name = "finish")
    private LocalDateTime finish;

    @Column(name = "description")
    private String description;

    @Column(name = "status_pay")
    @Enumerated(EnumType.STRING)
    private StatusPay statusPay;

    @Column(name = "status_washing")
    @Enumerated(EnumType.STRING)
    private StatusWashing statusWashing;

    @Column(name = "status_record")
    @Enumerated(EnumType.STRING)
    private StatusRecord statusRecord;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "car_wash_record_service",
            joinColumns = {@JoinColumn(name = "car_wash_record_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "service_id", insertable = false, updatable = false)})
    private Set<ServicePriceEntity> services = new HashSet<>();
}
