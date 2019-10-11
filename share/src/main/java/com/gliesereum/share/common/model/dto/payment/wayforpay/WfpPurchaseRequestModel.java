package com.gliesereum.share.common.model.dto.payment.wayforpay;

import com.gliesereum.share.common.model.dto.payment.enumerated.WfpPayTransactionType;
import com.gliesereum.share.common.model.dto.payment.enumerated.WfpTransactionType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class WfpPurchaseRequestModel {

    private List<String> productName;

    private List<Double> productPrice;

    private List<Integer> productCount;

    private UUID clientId;

    @NotNull
    private UUID orderId;

    @NotNull
    private Double amount;

    @NotNull
    private String currency;

    private String refundComment;

    private LocalDateTime orderDate;

    @NotNull
    private WfpTransactionType transactionType;

    private WfpPayTransactionType payTransactionType;

}
