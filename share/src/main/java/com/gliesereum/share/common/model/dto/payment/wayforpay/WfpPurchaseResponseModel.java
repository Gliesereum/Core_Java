package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionalStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class WfpPurchaseResponseModel {

    private WfpResponseCodeDto code;

    private WfpTransactionalStatus transactionStatus;

    private UUID orderId;

}
