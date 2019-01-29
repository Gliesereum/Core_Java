package com.gliesereum.share.common.model.dto.karma.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceDto extends DefaultDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private ServiceType serviceType;
}
