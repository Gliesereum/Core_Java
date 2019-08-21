package com.gliesereum.share.common.model.dto.language;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.language.enumerated.TextDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LitePackageDto extends DefaultDto {

    private String module;

    private String label;

    private String isoKey;

    private String icon;

    private TextDirection direction;
}
