package com.gliesereum.share.common.model.dto.account.user;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * @author vitalij
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CorporationEmployeeDto extends DefaultDto {

    private UUID corporationId;

    @NotNull
    private String firstName;

    private String lastName;

    @NotNull
    private String position;

}
