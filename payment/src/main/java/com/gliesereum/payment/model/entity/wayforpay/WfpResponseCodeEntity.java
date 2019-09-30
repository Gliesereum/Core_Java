package com.gliesereum.payment.model.entity.wayforpay;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author vitalij
 * @since 9/5/19
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "wfp_response_code")
public class WfpResponseCodeEntity extends DefaultEntity {

    @Column(name = "reason_code")
    private Integer reasonCode;

    @Column(name = "name")
    private String name;

    @Column(name = "reason")
    private String reason;

}