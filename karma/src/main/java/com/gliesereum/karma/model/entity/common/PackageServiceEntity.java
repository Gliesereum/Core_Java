package com.gliesereum.karma.model.entity.common;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "package_service")
public class PackageServiceEntity extends DefaultEntity {

    @Column(name = "package_id")
    private UUID packageId;

    @Column(name = "service_id")
    private UUID serviceId;

}
