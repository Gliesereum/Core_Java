package com.gliesereum.share.common.model.dto.mail;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MailStateDto extends DefaultDto {

    private String phone;

    private String text;

    private String messageId;

    private String httpStatus;

    private Integer messageStatus;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime create;

    public MailStateDto(String phone, String text, String messageId, String httpStatus, Integer messageStatus, LocalDateTime create) {
        this.phone = phone;
        this.text = text;
        this.messageId = messageId;
        this.httpStatus = httpStatus;
        this.messageStatus = messageStatus;
        this.create = create;
    }
}