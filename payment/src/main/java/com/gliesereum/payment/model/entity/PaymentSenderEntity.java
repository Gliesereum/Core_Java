package com.gliesereum.payment.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "payment_sender")
@EqualsAndHashCode(callSuper = true)
public class PaymentSenderEntity extends DefaultEntity {

    @Column(name = "object_id")
    private UUID objectId;

    @Column(name = "card_token")
    private String cardToken;

    @Column(name = "is_favorite")
    private boolean isFavorite;

}

