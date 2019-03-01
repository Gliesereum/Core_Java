package com.gliesereum.lendinggallery.model.entity.artbond;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "art_bond")
public class ArtBondEntity extends DefaultEntity {

    @Column(name = "price")
    private Integer price;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @Column(name = "execution")
    private String execution;

    @Column(name = "size")
    private String size;

    @Column(name = "dated")
    private String dated;

    @Column(name = "location")
    private String location;

    @Column(name = "state")
    private String state;

    @Column(name = "origin")
    private String origin;

    @Column(name = "exhibitions")
    private String exhibitions;

    @Column(name = "literature")
    private String literature;

    @Column(name = "status_type")
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(name = "special_service_type")
    @Enumerated(EnumType.STRING)
    private SpecialStatusType specialStatusType;

    @ElementCollection
    @CollectionTable(name="art_bond_tag", joinColumns=@JoinColumn(name="art_bond_id"))
    @Column(name="tag")
    private List<String> tags;

}
