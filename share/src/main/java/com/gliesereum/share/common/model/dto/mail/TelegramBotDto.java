package com.gliesereum.share.common.model.dto.mail;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TelegramBotDto extends DefaultDto {

    private Long chatId;

    private String chatName;

    private Boolean active;
}
