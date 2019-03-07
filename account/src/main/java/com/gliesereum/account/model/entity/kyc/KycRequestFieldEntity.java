package com.gliesereum.account.model.entity.kyc;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kyc_request_field")
public class KycRequestFieldEntity extends DefaultEntity {

    @Column(name = "kyc_field_id")
    private UUID kycFieldId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kyc_field_id", insertable = false, updatable = false)
    private KycFieldEntity kycField;

    @Column(name = "kyc_request_id")
    private UUID kycRequestId;

    @Column(name = "value")
    private String value;
}
