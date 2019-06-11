package com.gliesereum.share.common.model.dto.lendinggallery.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPaymentInfo {

    private Double balance = 0.0;

    private Double profit = 0.0;

    private Double resultProfit = 0.0;

    private Double nkd = 0.0;
}
