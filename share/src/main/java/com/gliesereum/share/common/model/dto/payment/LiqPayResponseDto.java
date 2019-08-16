package com.gliesereum.share.common.model.dto.payment;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vitalij
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class LiqPayResponseDto {

    private Object data;

    private Object signature;
}
