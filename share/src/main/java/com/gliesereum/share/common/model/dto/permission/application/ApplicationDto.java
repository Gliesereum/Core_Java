package com.gliesereum.share.common.model.dto.permission.application;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ApplicationDto extends DefaultDto {

    private String name;

    private String description;

    private Boolean isActive;

    private List<String> hosts;
}
