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
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "filter_attribute")
public class FilterAttributeEntity extends DefaultEntity {

    @Column(name = "value")
    private String value;

    @Column(name = "title")
    private String title;

    @Column(name = "filter_id")
    private UUID filterId;

}
