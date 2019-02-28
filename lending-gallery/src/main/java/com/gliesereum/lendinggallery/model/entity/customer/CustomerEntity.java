package com.gliesereum.lendinggallery.model.entity.customer;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.CustomerType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.OriginFunds;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
public class CustomerEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "name")
    private String name;

    @Column(name = "place_birth")
    private String placeBirth;

    @Column(name = "date_birth")
    private LocalDateTime dateBirth;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "passport")
    private String passport;

    @Column(name = "position")
    private String position;

    @Column(name = "amount_investment")
    private Integer amountInvestment;

    @Column(name = "origin_funds")
    @Enumerated(EnumType.STRING)
    private OriginFunds originFunds;

    @Column(name = "customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
}



