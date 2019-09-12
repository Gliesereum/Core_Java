package com.gliesereum.karma.model.entity.filter.descriptions;

import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "filter_attribute_description")
public class FilterAttributeDescriptionEntity extends BaseDescriptionEntity {

    @Column(name = "title")
    private String title;

}
