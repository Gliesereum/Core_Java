package com.gliesereum.account.model.entity.kyc;

import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
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
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kyc_request")
public class KycRequestEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "kyc_request_type")
    @Enumerated(EnumType.STRING)
    private KycRequestType kycRequestType;

    @Column(name = "kyc_status")
    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    @Column(name = "comment")
    private String comment;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany
    @JoinColumn(name = "kyc_request_id", insertable = false, updatable = false)
    private Set<KycRequestFieldEntity> fields = new HashSet<>();
}
