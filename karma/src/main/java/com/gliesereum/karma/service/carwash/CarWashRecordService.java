package com.gliesereum.karma.service.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashRecordService extends DefaultService<CarWashRecordDto, CarWashRecordEntity> {

    List<CarWashRecordDto> getByParamsForClient(Map<String, String> params);

    List<CarWashRecordDto> getByParamsForBusiness(Map<String, String> params);
}
