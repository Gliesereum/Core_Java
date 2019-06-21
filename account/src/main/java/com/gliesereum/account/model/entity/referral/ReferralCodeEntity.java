package com.gliesereum.account.model.entity.referral;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "referral_code")
@EntityListeners(AuditingEntityListener.class)
public class ReferralCodeEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "code")
    private String code;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

}
