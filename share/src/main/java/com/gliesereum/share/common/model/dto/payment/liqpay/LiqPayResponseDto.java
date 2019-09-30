package com.gliesereum.share.common.model.dto.payment.liqpay;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LiqPayResponseDto {

    private String data;

    private String signature;

}
