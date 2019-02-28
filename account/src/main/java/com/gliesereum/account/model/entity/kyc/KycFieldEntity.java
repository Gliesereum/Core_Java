package com.gliesereum.account.model.entity.kyc;

import com.gliesereum.share.common.model.dto.account.enumerated.KycFieldType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "kyc_field")
public class KycFieldEntity extends DefaultEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "field_type")
    @Enumerated(EnumType.STRING)
    private KycFieldType fieldType;

    @Column(name = "kyc_request_type")
    @Enumerated(EnumType.STRING)
    private KycRequestType requestType;
}
