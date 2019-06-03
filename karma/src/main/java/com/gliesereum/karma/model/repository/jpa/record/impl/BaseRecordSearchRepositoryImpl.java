package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordSearchRepository;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public class BaseRecordSearchRepositoryImpl implements BaseRecordSearchRepository {

    private static final String GET_BY_TIME_BETWEEN_QUERY =
            "SELECT r.* " +
            "FROM karma.record as r " +
            "LEFT JOIN karma.business as b on r.business_id=b.id " +
            "WHERE (r.begin between (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone + :minutesFrom) * interval '1 minute')) AND (to_timestamp(:from, 'YYYY-MM-DD HH24:MI:SS') + ((b.time_zone + :minutesTo) * interval '1 minute'))) AND " +
            "r.status_record = :status AND r.notification_send = :notificationSend";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<BaseRecordEntity> getByTimeBetween(LocalDateTime from, Integer minutesFrom, Integer minutesTo, StatusRecord status, boolean notificationSend) {
        List<BaseRecordEntity> result = null;
        if (ObjectUtils.allNotNull(from, minutesFrom, minutesTo, status, notificationSend)) {
            Query query = entityManager.createNativeQuery(GET_BY_TIME_BETWEEN_QUERY, BaseRecordEntity.class);
            query.setParameter("from", from.toString());
            query.setParameter("minutesFrom", minutesFrom);
            query.setParameter("minutesTo", minutesTo);
            query.setParameter("status", status.name());
            query.setParameter("notificationSend", notificationSend);
            result = query.getResultList();
        }
        return result;
    }
}
