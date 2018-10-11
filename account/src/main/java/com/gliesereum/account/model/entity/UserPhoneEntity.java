package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "user_phone")
public class UserPhoneEntity extends DefaultEntity {

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private UserEntity user;
}
