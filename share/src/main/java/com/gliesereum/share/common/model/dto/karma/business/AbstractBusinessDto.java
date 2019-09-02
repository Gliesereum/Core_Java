package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.AuditableDefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AbstractBusinessDto extends AuditableDefaultDto {

    private UUID corporationId;

    @NotEmpty
    private String name;

    private String description;

    private String logoUrl;

    private String coverUrl;

    private String siteUrl;

    private boolean cashlessPayment;

    private Boolean businessVerify;

    @NotEmpty
    @Size(min = 5)
    private String address;

    @NotEmpty
    @Size(min = 5, max = 13)
    private String phone;

    @Size(min = 5, max = 13)
    private String addPhone;

    @NotNull
    @Max(90)
    @Min(-90)
    private Double latitude;

    @NotNull
    @Max(180)
    @Min(-180)
    private Double longitude;

    @NotNull
    @Max(720)
    @Min(-720)
    private Integer timeZone;

    private UUID businessCategoryId;

    private BusinessCategoryDto businessCategory;

}
