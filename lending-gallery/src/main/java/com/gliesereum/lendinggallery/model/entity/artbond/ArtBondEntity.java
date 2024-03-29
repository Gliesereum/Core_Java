package com.gliesereum.lendinggallery.model.entity.artbond;

import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.entity.AuditableDefaultEntity;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDateTime;
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
public class ArtBondEntity extends AuditableDefaultEntity {

    @Column(name = "price")
    private Integer price;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Formula(value = "COALESCE(price, 0) / COALESCE(stock_count, 1)")
    private Double stockPrice;

    @Formula(value = "(COALESCE(price, 0) / COALESCE(stock_count, 1)) / 100 * COALESCE(dividend_percent, 0)")
    private Double baseDividend;

    @Column(name = "dividend_percent")
    private Integer dividendPercent;

    @Column(name = "reward_percent")
    private Integer rewardPercent;

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

    @Column(name = "article")
    private String article;

    @Column(name = "expertise")
    private String expertise;

    @Column(name = "blockchain")
    private String blockchain;

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

    @Column(name = "payment_period")
    private Integer paymentPeriod;

    @Column(name = "payment_start_date")
    private LocalDateTime paymentStartDate;

    @Column(name = "payment_finish_date")
    private LocalDateTime paymentFinishDate;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_url")
    private String coverUrl;

    @Column(name = "video_url")
    private String videoUrl;

}
