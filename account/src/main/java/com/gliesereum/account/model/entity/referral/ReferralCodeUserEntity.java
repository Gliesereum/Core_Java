package com.gliesereum.account.model.entity.referral;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "referral_code_user")
public class ReferralCodeUserEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "referrer_id")
    private UUID referrerId;

    @Column(name = "referral_code_id")
    private UUID referralCodeId;
}