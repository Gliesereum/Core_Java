package com.gliesereum.share.common.model.dto.lendinggallery.artbond;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonDeserializer;
import com.gliesereum.share.common.databind.json.LocalDateTimeJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SpecialStatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.StatusType;
import com.gliesereum.share.common.model.dto.lendinggallery.media.MediaDto;
import com.gliesereum.share.common.model.dto.lendinggallery.payment.PaymentCalendarDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtBondDto extends DefaultDto {

    private Integer price;

    private Integer stockCount;

    private Double stockPrice;

    private Double baseDividend;

    private Integer dividendPercent;

    private Integer rewardPercent;

    private String name;

    private String description;

    private String author;

    private String execution;

    private String size;

    private String dated;

    private String location;

    private String state;

    private String origin;

    private String exhibitions;

    private String literature;

    private StatusType statusType;

    private SpecialStatusType specialStatusType;

    private List<String> tags;

    private List<MediaDto> images = new ArrayList<>();

    private List<MediaDto> authorInfo = new ArrayList<>();

    private List<MediaDto> artBondInfo = new ArrayList<>();

    private List<MediaDto> documents = new ArrayList<>();

    private Integer paymentPeriod;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime paymentStartDate;

    @JsonDeserialize(using = LocalDateTimeJsonDeserializer.class)
    @JsonSerialize(using = LocalDateTimeJsonSerializer.class)
    private LocalDateTime paymentFinishDate;

    private List<PaymentCalendarDto> paymentCalendar = new ArrayList<>();

    private Double nkd;

    private Map<String, Integer> percentPerYear;

    private Double amountCollected;

}
