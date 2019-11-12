package com.gliesereum.lendinggallery.service.artbond;

import com.gliesereum.lendinggallery.model.entity.artbond.ArtBondEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.ArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.DetailedArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.artbond.InterestedArtBondDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import com.gliesereum.share.common.model.query.lendinggallery.artbond.ArtBondQuery;
import com.gliesereum.share.common.service.DefaultService;
import com.gliesereum.share.common.service.auditable.AuditableService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ArtBondService extends AuditableService<ArtBondDto, ArtBondEntity> {

    List<ArtBondDto> getAllByStatus(StatusType status);

    ArtBondDto getArtBondById(UUID id);

    ArtBondDto updateStatus(StatusType status, UUID id);

    List<ArtBondDto> getAllByTags(List<String> tags);

    List<ArtBondDto> getAllByUser();

    List<DetailedArtBondDto> getDetailedAll();

    Map<String, Double> currencyExchange(Long sum);

    List<PaymentCalendarDto> getPaymentCalendar(UUID id, boolean setArtBond);

    List<PaymentCalendarDto> getPaymentCalendar(ArtBondDto artBond, boolean setArtBond);

    List<PaymentCalendarDto> getPaymentCalendar(ArtBondDto artBond, LocalDateTime paymentStartDate, Long stockCount, boolean setArtBond);

    Double getNkd(UUID artBondId);

    Double getNkd(ArtBondDto artBond);

    double calculateNkd(double dividendValuePerYear, long daysPerPaymentYear, long daysAfterLastPayment, double rewardValue, long daysPayment, long daysAfterPaymentStart);

    Map<String, Integer> getPercentPerYear(UUID artBondId);

    Map<String, Integer> getPercentPerYear(ArtBondDto artBond);

    Double getAmountCollected(UUID id);

    ArtBondDto superUpdateArtBond(ArtBondDto dto);

    List<ArtBondDto> search(ArtBondQuery searchQuery);

    InterestedArtBondDto interested(UUID id);

    void notInterested(UUID id);

    List<InterestedArtBondDto> getInterested(UUID id);

    List<ArtBondDto> getAllByCurrentAdvisor();

    List<ArtBondDto> getAllByAdvisor(UUID id);
}
