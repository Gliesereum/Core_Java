package com.gliesereum.share.common.model.dto.payment.enumerated;

/**
 * @author vitalij
 * @since 9/5/19
 */
public enum WfpTransactionalStatus {

    Created,
    InProcessing,
    WaitingAuthComplete,
    Approved,
    Pending,
    Expired,
    Declined,
    RefundInProcessing,
    Refunded, Voided; //todo maybe Refunded/Voided
}
