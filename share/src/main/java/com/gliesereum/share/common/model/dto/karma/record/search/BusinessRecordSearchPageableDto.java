package com.gliesereum.share.common.model.dto.karma.record.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class BusinessRecordSearchPageableDto extends BusinessRecordSearchDto {

    @Min(0)
    private Integer page;

    @Min(1)
    private Integer size;

    private Sort.Direction sortDirection;

    private BusinessRecordSearchOrderField sortField;

}