package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface BaseRecordAnalyticRepository {

    List<BaseRecordEntity> getRecordsByFilter(AnalyticFilterDto filter);
}
