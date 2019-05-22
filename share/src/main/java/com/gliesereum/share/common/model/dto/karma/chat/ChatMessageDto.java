package com.gliesereum.share.common.model.dto.karma.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatMessageDto extends DefaultDto {

    private UUID chatId;

    private String message;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime createDate;

    public ChatMessageDto(UUID chatId, String message, LocalDateTime createDate) {
        this.chatId = chatId;
        this.message = message;
        this.createDate = createDate;
    }
}