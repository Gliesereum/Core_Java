package com.gliesereum.share.common.model.dto.lendinggallery.payment;

import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class PaymentCalendarDto {

    private Double dividendValue;

    private Integer dividendPercent;

    private LocalDateTime date;

    private ArtBondDto artBond;
}
