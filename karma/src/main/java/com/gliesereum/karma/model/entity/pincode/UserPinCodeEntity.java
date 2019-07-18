package com.gliesereum.karma.model.entity.pincode;

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
@Table(name = "user_pin_code")
public class UserPinCodeEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "pin_code")
    private String pinCode;
}