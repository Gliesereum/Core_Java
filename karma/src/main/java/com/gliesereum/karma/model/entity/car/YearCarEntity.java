package com.gliesereum.karma.model.entity.car;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "year_car")
public class YearCarEntity extends DefaultEntity {

    @Column(name = "year_value")
    private int yearValue;
}
