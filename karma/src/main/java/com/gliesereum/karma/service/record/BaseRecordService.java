package com.gliesereum.karma.service.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusPay;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.LiteRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
import com.gliesereum.share.common.service.DefaultService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface BaseRecordService extends DefaultService<BaseRecordDto, BaseRecordEntity> {

    List<BaseRecordDto> getByParamsForClient(RecordsSearchDto search);

    List<BaseRecordDto> getByBusinessIdAndStatusRecord(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to);

    List<BaseRecordDto> getByBusinessIdAndStatusRecordNotificationSend(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to, boolean notificationSend);

    List<BaseRecordDto> getByParamsForBusiness(RecordsSearchDto search);

    BaseRecordDto updateWorkingSpace(UUID idRecord, UUID workingSpaceId);

    BaseRecordDto updateStatusProgress(UUID idRecord, StatusProcess status);

    BaseRecordDto canceledRecord(UUID idRecord);

    BaseRecordDto updateTimeRecord(UUID idRecord, Long beginTime);

    BaseRecordDto getFreeTimeForRecord(BaseRecordDto dto);

    BaseRecordDto superCreateRecord(BaseRecordDto dto);

    List<BaseRecordDto> getAllByUser();

    BaseRecordDto createFromBusiness(BaseRecordDto dto);

    BaseRecordDto getFullModelById(UUID id);

    BaseRecordDto getFullModelByIdWithPermission(UUID id);

    BaseRecordDto updateStatusPay(UUID idRecord, StatusPay status);

    List<LiteRecordDto> convertToLiteRecordDto(List<BaseRecordEntity> entities);

    List<LiteRecordDto> getLiteRecordDtoByBusiness(UUID businessId, List<StatusRecord> statuses, Long from, Long to);

    void setFullModelRecord(List<BaseRecordDto> list);

    void setNotificationSend(UUID recordId);
}
