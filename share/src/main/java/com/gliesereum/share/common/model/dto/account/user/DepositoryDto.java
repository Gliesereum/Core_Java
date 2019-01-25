package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepositoryDto extends DefaultDto {

    @NotEmpty
    private String path;

    @NotEmpty
    private UUID ownerId;

    public DepositoryDto(@NotEmpty String path, @NotEmpty UUID ownerId) {
        this.path = path;
        this.ownerId = ownerId;
    }
}
