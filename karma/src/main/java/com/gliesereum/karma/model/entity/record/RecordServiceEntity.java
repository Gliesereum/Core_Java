package com.gliesereum.karma.model.entity.record;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "record_service")
public class RecordServiceEntity extends DefaultEntity {

    @Column(name = "record_id")
    private UUID recordId;

    @Column(name = "service_id")
    private UUID serviceId;

}
