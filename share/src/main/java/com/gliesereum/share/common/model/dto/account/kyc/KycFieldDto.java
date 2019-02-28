package com.gliesereum.share.common.model.dto.account.kyc;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.account.enumerated.KycFieldType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class KycFieldDto extends DefaultDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String title;

    private String description;

    @NotEmpty
    private String placeholder;

    @NotNull
    private KycFieldType fieldType;

    @NotNull
    private KycRequestType requestType;
}
