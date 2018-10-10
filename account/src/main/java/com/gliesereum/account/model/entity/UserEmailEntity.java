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
@Table(name = "user_email")
public class UserEmailEntity extends DefaultEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "user_id")
    private UUID userId;
}
