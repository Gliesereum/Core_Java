package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.dto.account.enumerated.KFCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class UserEntity extends DefaultEntity {

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "ban_status")
    @Enumerated(EnumType.STRING)
    private BanStatus banStatus;

    @Column(name = "verified_status")
    @Enumerated(EnumType.STRING)
    private VerifiedStatus verifiedStatus;

    @Column(name = "kfc_status")
    @Enumerated(EnumType.STRING)
    private KFCStatus kfcStatus;
}
