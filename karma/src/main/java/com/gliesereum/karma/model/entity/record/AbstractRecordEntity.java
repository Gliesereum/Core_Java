package com.gliesereum.karma.model.entity.record;

import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@MappedSuperclass
public class AbstractRecordEntity extends DefaultEntity {

    @Column(name = "client_id")
    private UUID clientId;

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

    @Column(name = "business_category_id")
    private UUID businessCategoryId;

    @Column(name = "notification_send")
    private boolean notificationSend;
}
