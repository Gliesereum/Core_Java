package com.gliesereum.share.common.model.dto.karma.carwash;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.common.ServiceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusWashing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
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

    private UUID carWashId;

    private Integer price;

    private Time beginTime;

    private Time finishTime;

    private LocalDate date;

    private Integer numberBox;

    private StatusPay statusPay;

    private StatusWashing statusWashing;

    private StatusRecord statusRecord;

    private List<ServiceDto> services = new ArrayList<>();
}
