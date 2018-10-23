package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user_phone")
public class UserPhoneEntity extends DefaultEntity {

    @Column(name = "phone")
    private String phone;

    @Column(name = "user_id")
    private UUID userId;

}
