package com.gliesereum.mail.model.entity;

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
@Table(name = "feed_back_user")
public class FeedBackUserEntity extends DefaultEntity {

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "app_id")
    private UUID appId;
}