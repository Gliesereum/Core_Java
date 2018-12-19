package com.gliesereum.share.common.model.dto.karma.carwash;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusWashing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CarWashRecordDto extends DefaultDto {

    private UUID carId;

    private UUID packageId;

    private UUID workingSpaceId;

    private UUID carWashId;

    private Integer price;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime beginTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime finishTime;

    @JsonFormat(pattern = "yyyy:MM:dd")
    private LocalDate date;

    private String description;

    private StatusPay statusPay;

    private StatusWashing statusWashing;

    private StatusRecord statusRecord;

    private List<ServicePriceDto> services = new ArrayList<>();
}
