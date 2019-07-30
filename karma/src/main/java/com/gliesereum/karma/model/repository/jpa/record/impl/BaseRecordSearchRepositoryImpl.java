package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.car.CarEntity;
import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.entity.record.BaseRecordPageEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordSearchRepository;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public BaseRecordPageEntity getRecordsBySearchDto(RecordsSearchDto search) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseRecordEntity> query = builder.createQuery(BaseRecordEntity.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        createEqIfNotNull(builder, predicates, root.get("phone"), search.getPhone());
        createEqIfNotNull(builder, predicates, root.get("recordNumber"), search.getRecordNumber());
        createBetweenDate(builder, predicates, root.get("begin"), search.getFrom(), search.getTo());
        createLikeIfNotNull(builder, predicates, root.get("lastName"), search.getLastName());
        createLikeIfNotNull(builder, predicates, root.get("firstName"), search.getFirstName());
        createLikeIfNotNull(builder, predicates, root.get("phone"), search.getPhone());
        createInIfNotEmpty(predicates, root.get("statusRecord"), search.getStatus());
        createInIfNotEmpty(predicates, root.get("statusProcess"), search.getProcesses());
        createInIfNotEmpty(predicates, root.get("businessId"), search.getBusinessIds());

        if (search.getRegistrationNumber() != null) {
            Subquery<UUID> subQuery = query.subquery(UUID.class);
            Root<CarEntity> subRoot = subQuery.from(CarEntity.class);
            Predicate targetIdEq = builder.equal(subRoot.get("id"), root.get("targetId"));
            Predicate numberIdEq = builder.equal(subRoot.get("registrationNumber"), search.getRegistrationNumber());
            subQuery.select(subRoot.get("id"));
            subQuery.where(targetIdEq, numberIdEq);
            predicates.add(root.get("targetId").in(subQuery));
        }
        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        TypedQuery<BaseRecordEntity> typedQuery = entityManager.createQuery(query);
        BaseRecordPageEntity result = new BaseRecordPageEntity();
        result.setCount(typedQuery.getResultList().size());
        if (search.getMaxResult() > 0) {
            typedQuery.setFirstResult(search.getFirstResult());
            typedQuery.setMaxResults(search.getMaxResult());
        }
        result.setRecords(typedQuery.getResultList());
        return result;
    }


    private void createLikeIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<String> expression, String value) {
        if (value != null) {
            predicates.add(criteriaBuilder.like(expression, value));
        }
    }

    private void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
    }

    private void createInIfNotEmpty(List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(expression.in(value));
        }
    }

    private void createBetweenDate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends LocalDateTime> expression, LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
        }
        if (to != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
        }
    }

}
