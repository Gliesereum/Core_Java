package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
public class UserEntity extends DefaultEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "address")
    private String address;

    @Column(name = "add_address")
    private String addAddress;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "ban_status")
    @Enumerated(EnumType.STRING)
    private BanStatus banStatus;

    @Column(name = "kyc_approved")
    private Boolean kycApproved;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_corporation",
            joinColumns = {@JoinColumn(name = "user_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "corporation_id", insertable = false, updatable = false)})
    private Set<CorporationEntity> corporation = new HashSet<>();
}
