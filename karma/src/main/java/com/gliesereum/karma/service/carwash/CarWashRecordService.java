package com.gliesereum.karma.service.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusWashing;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashRecordService extends DefaultService<CarWashRecordDto, CarWashRecordEntity> {

    List<CarWashRecordDto> getByParamsForClient(Map<String, String> params);

    List<CarWashRecordDto> getByParamsForBusiness(Map<String, String> params);

    CarWashRecordDto updateWashingSpace(UUID idRecord, UUID workingSpaceId, Boolean isUser);

    CarWashRecordDto updateStatusWashing(UUID idRecord, StatusWashing status, Boolean isUser);

    CarWashRecordDto updateStatusRecord(UUID idRecord, StatusRecord status, Boolean isUser);

    CarWashRecordDto updateTimeRecord(UUID idRecord, String beginTime, Boolean isUser);
}
