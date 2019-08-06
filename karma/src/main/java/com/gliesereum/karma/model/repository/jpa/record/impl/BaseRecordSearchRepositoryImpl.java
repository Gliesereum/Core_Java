package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordSearchRepository;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchDto;
import com.gliesereum.share.common.model.entity.DefaultEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

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
    public Page<BaseRecordEntity> getRecordsBySearchDto(BusinessRecordSearchDto search, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseRecordEntity> query = builder.createQuery(BaseRecordEntity.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (search != null) {
            createEqIfNotNull(builder, predicates, root.get("recordNumber"), search.getRecordNumber());
            createBetweenDate(builder, predicates, root.get("begin"), search.getFrom(), search.getTo());
            createInIfNotEmpty(predicates, root.get("statusRecord"), search.getStatus());
            createInIfNotEmpty(predicates, root.get("statusProcess"), search.getProcesses());
            createInIfNotEmpty(predicates, root.get("businessId"), search.getBusinessIds());
            createInIfNotEmpty(predicates, root.get("clientId"), search.getClientIds());
            createInIfNotEmpty(predicates, root.get("workingSpaceId"), search.getWorkingSpaceIds());
        }

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }

        query.orderBy(toOrders(pageable.getSortOr(Sort.by(Sort.Direction.DESC, "begin")), root, builder));

        TypedQuery<BaseRecordEntity> typedQuery = entityManager.createQuery(query);

        if (pageable.isPaged()) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }

        return PageableExecutionUtils.getPage(typedQuery.getResultList(), pageable,
                () -> executeCountQuery(entityManager.createQuery(getCountQuery(predicates, BaseRecordEntity.class))));
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
        if (ObjectUtils.allNotNull(from, to)) {
            predicates.add(criteriaBuilder.between(expression, from, to));
        } else {
            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
            }
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
            }
        }
    }

    private <E extends DefaultEntity> CriteriaQuery<Long> getCountQuery(List<Predicate> predicates, Class<E> entity) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<E> root = query.from(entity);
        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        if (query.isDistinct()) {
            query.select(builder.countDistinct(root));
        } else {
            query.select(builder.count(root));
        }
        return query;
    }

    private long executeCountQuery(TypedQuery<Long> query) {
        Assert.notNull(query, "TypedQuery must not be null!");
        List<Long> totals = query.getResultList();
        long total = 0L;
        for (Long element : totals) {
            total += element == null ? 0 : element;
        }
        return total;
    }

}
