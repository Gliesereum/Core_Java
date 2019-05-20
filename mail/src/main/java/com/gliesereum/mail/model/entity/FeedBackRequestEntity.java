package com.gliesereum.mail.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "feed_back_request")
public class FeedBackRequestEntity extends DefaultEntity {

    @Column(name = "request_processed")
    private boolean requestProcessed;

    @Column(name = "phone")
    private String phone;

    @Column(name = "app_id")
    private UUID appId;

    @Column(name = "create_date")
    private LocalDateTime createDate;
}