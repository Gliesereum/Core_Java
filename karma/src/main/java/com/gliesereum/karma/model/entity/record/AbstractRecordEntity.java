package com.gliesereum.karma.model.entity.record;

import com.gliesereum.karma.model.entity.service.ServicePriceEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
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
@MappedSuperclass
public class AbstractRecordEntity extends DefaultEntity {

    @Column(name = "target_id")
    private UUID targetId;

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "business_id")
    private UUID businessId;

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

    @Column(name = "status_process")
    @Enumerated(EnumType.STRING)
    private StatusProcess statusProcess;

    @Column(name = "status_record")
    @Enumerated(EnumType.STRING)
    private StatusRecord statusRecord;

    @Column(name = "service_type")
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "record_service",
            joinColumns = {@JoinColumn(name = "record_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "service_id", insertable = false, updatable = false)})
    private Set<ServicePriceEntity> services = new HashSet<>();
}
