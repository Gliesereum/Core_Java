package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.common.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.common.RecordsSearchDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
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

    List<BaseRecordDto> getByParamsForCorporation(RecordsSearchDto search);

    BaseRecordDto updateWorkingSpace(UUID idRecord, UUID workingSpaceId, Boolean isUser);

    BaseRecordDto updateStatusProgress(UUID idRecord, StatusProcess status, Boolean isUser);

    BaseRecordDto updateStatusRecord(UUID idRecord, StatusRecord status, Boolean isUser);

    BaseRecordDto updateTimeRecord(UUID idRecord, Long beginTime, Boolean isUser);

    BaseRecordDto getFreeTimeForRecord(BaseRecordDto dto);

    List<BaseRecordDto> getAllByUser(ServiceType serviceType);
}
