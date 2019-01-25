package com.gliesereum.share.common.model.dto.karma.comment;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class RatingDto {

    private BigDecimal rating;

    private Integer count;
}
