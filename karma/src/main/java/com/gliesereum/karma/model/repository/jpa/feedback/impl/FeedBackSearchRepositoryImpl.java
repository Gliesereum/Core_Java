package com.gliesereum.karma.model.repository.jpa.feedback.impl;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import com.gliesereum.karma.model.repository.jpa.feedback.FeedBackSearchRepository;
import com.gliesereum.share.common.model.dto.karma.feedback.FeedBackSearchDto;
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
public class FeedBackSearchRepositoryImpl implements FeedBackSearchRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<FeedBackUserEntity> getBySearch(FeedBackSearchDto search) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<FeedBackUserEntity> query = builder.createQuery(FeedBackUserEntity.class);
        Root<FeedBackUserEntity> root = query.from(FeedBackUserEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        createBetweenDate(builder, predicates, root.get("dateCreateObject"), search.getDateCreateObjectFrom(), search.getDateCreateObjectTo());
        createBetweenDate(builder, predicates, root.get("dateFeedback"), search.getDateFeedBackFrom(), search.getDateFeedBackTo());
        createEqIfNotNull(builder, predicates, root.get("objectId"), search.getObjectId());
        createEqIfNotNull(builder, predicates, root.get("workingSpaceId"), search.getWorkingSpaceId());
        createEqIfNotNull(builder, predicates, root.get("workerId"), search.getWorkerId());
        createInIfNotEmpty(predicates, root.get("businessId"), search.getBusinessIds());
        createBetweenOrGreaterOrLess(builder, predicates, root.get("mark"), search.getMarkFrom(), search.getMarkTo());
        TypedQuery<FeedBackUserEntity> typedQuery = entityManager.createQuery(query);
        return typedQuery.getResultList();
    }

    private void createBetweenDate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<? extends LocalDateTime> expression, LocalDateTime from, LocalDateTime to) {
        if (from != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
        }
        if (to != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
        }
    }

    private void createInIfNotEmpty(List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(expression.in(value));
        }
    }

    private void createEqIfNotNull(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, Expression<?> expression, Object value) {
        if (value != null) {
            predicates.add(criteriaBuilder.equal(expression, value));
        }
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
}
