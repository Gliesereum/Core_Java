package com.gliesereum.share.common.model.dto.karma.faq;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FaqDto extends DefaultDto {

    private String title;

    private String description;

    private Integer index;
}