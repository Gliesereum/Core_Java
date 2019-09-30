package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author vitalij
 * @since 9/5/19
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WfpResponseCodeDto extends DefaultDto {

    private Integer reasonCode;

    private String name;

    private String reason;

}
