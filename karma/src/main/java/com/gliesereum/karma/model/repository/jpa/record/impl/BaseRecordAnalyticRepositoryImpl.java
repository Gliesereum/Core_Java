package com.gliesereum.karma.model.repository.jpa.record.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordAnalyticRepository;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public class BaseRecordAnalyticRepositoryImpl implements BaseRecordAnalyticRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BaseRecordEntity> getRecordsByFilter(AnalyticFilterDto filter) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BaseRecordEntity> query = builder.createQuery(BaseRecordEntity.class);
        Root<BaseRecordEntity> root = query.from(BaseRecordEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        createBetweenOrGreaterOrLess(builder, predicates, root.get("price"), filter.getPriceMin(), filter.getPriceMax());
        createBetweenDate(builder, predicates, root.get("begin"), filter.getFrom(), filter.getTo());
        createEqIfNotNull(builder, predicates, root.get("businessId"), filter.getBusinessId());
        createEqIfNotNull(builder, predicates, root.get("clientId"), filter.getClientId());
        createInIfNotEmpty(predicates, root.get("statusRecord"), filter.getStatuses());

        if (CollectionUtils.isNotEmpty(predicates)) {
            query.where(predicates.toArray(new Predicate[predicates.size()]));
        }
        TypedQuery<BaseRecordEntity> result = entityManager.createQuery(query);
        return result.getResultList();
    }


    private void createBetweenOrGreaterOrLess(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Integer> expression, Integer greater, Integer less) {
        if (ObjectUtils.allNotNull(greater, less)) {
            predicates.add(criteriaBuilder.between(expression, greater, less));
        } else {
            createGreaterThanIfNotNull(criteriaBuilder, predicates, expression, greater);
            createLessThanIfNotNull(criteriaBuilder, predicates, expression, less);
        }
    }

    private void createGreaterThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.gt(expression, value));
        }
    }

    private void createLessThanIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends Number> expression, Number value) {
        if (value != null) {
            predicates.add(criteriaBuilder.lt(expression, value));
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
