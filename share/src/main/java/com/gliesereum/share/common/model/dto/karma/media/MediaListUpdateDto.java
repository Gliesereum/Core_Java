package com.gliesereum.share.common.model.dto.karma.media;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class MediaListUpdateDto {
    
    @NotNull
    private UUID objectId;
    
    @Valid
    private List<MediaDto> list;
}
