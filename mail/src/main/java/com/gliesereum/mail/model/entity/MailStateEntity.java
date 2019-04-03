package com.gliesereum.mail.model.entity;

import com.gliesereum.share.common.model.entity.DefaultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mail_state")
public class MailStateEntity extends DefaultEntity {

    @Column(name = "phone")
    private String phone;

    @Column(name = "text")
    private String text;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "http_status")
    private String httpStatus;

    @Column(name = "message_status")
    private Integer messageStatus;

    @Column(name = "create_date")
    private LocalDateTime create;

}