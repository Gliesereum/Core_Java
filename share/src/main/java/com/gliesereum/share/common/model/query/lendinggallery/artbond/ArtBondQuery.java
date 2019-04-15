package com.gliesereum.share.common.model.query.lendinggallery.artbond;

import com.gliesereum.share.common.model.query.base.OrderType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class ArtBondQuery {

    private Integer from;

    private Integer size;

    private String nameEq;

    private Integer priceGreaterThan;

    private Integer priceLessThan;

    private Double stockPriceGreaterThan;

    private Double stockPriceLessThan;

    private Integer dividendPercentGreaterThan;

    private Integer dividendPercentLessThan;

    private Integer rewardPercentGreaterThan;

    private Integer rewardPercentLessThan;

    private Double amountCollectedGreaterThan;

    private Double amountCollectedLessThan;

    private OrderType orderType;

    private String orderField;
}
