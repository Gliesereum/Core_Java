package com.gliesereum.share.common.model.dto.karma.pincode;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserPinCodeDto extends DefaultDto {

    private UUID userId;

    @Min(1000)
    @Max(9999)
    @NotNull
    private Integer pinCode;
}
