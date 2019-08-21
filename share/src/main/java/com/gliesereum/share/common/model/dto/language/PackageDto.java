package com.gliesereum.share.common.model.dto.language;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.language.enumerated.TextDirection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PackageDto extends DefaultDto {

    @NotBlank
    private String module;

    @NotBlank
    private String label;

    @NotBlank
    private String isoKey;

    private String icon;

    @NotNull
    private TextDirection direction;

    private List<PhraseDto> phrases = new ArrayList<>();
}
