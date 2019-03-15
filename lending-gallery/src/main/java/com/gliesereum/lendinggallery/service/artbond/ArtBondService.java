package com.gliesereum.lendinggallery.service.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.service.DefaultService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondService extends DefaultService<ArtBondDto, ArtBondEntity> {

    List<ArtBondDto> getAllByStatus(StatusType status);

    ArtBondDto getArtBondById(UUID id);

    ArtBondDto updateStatus(StatusType status, UUID id);

    List<ArtBondDto> getAllByTags(List<String> tags);

    List<ArtBondDto> getAllByUser();

    Map<String, Integer> currencyExchange(Long sum);

    List<PaymentCalendarDto> getPaymentCalendar(UUID id);

    List<PaymentCalendarDto> getPaymentCalendar(ArtBondDto artBond, LocalDateTime paymentStartDate, Long stockCount);
}
