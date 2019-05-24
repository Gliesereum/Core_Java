package com.gliesereum.share.common.model.dto.karma.chat;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChatDto extends DefaultDto {

    @NonNull
    private UUID userId;

    @NonNull
    private UUID businessId;

    public ChatDto(UUID userId, UUID businessId) {
        this.userId = userId;
        this.businessId = businessId;
    }
}