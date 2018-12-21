package com.gliesereum.share.common.model.dto.karma.carwash;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
public class CarWashRecordFreeTimesModel {

    private CarWashRecordDto record;

    private LocalDateTime time;

    private UUID spaceID;

}
