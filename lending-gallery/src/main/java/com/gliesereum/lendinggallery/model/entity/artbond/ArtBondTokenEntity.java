package com.gliesereum.lendinggallery.model.entity.artbond;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "art_bond_token")
public class ArtBondTokenEntity extends DefaultEntity {

    @Column(name = "blockchain")
    private String blockchain;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "art_bond_id")
    private UUID artBondId;
}