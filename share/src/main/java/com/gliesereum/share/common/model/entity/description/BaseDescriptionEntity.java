package com.gliesereum.share.common.model.entity.description;

import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public class BaseDescriptionEntity extends DefaultEntity {

    @Column(name = "language_code")
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;

    @Column(name = "object_id")
    private UUID objectId;
}
