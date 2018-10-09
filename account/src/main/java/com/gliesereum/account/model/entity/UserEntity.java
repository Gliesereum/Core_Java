package com.gliesereum.account.model.entity;

import com.gliesereum.share.common.model.dto.account.enumerated.Gender;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
@Data
@NoArgsConstructor
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
}
